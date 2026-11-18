package com.example.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.CurriculumCatalog
import com.example.data.model.LessonProgress
import com.example.data.model.RoutineItem
import com.example.data.model.RoutineLog
import com.example.data.repository.SunnyLearnRepository
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}

data class SunnyDashboardData(
    val routines: List<RoutineItem>,
    val routineLogs: List<RoutineLog>,
    val lessonProgress: List<LessonProgress>,
    val todayDateKey: String,
    val todayDayOfWeek: Int,
) {
    val completedRoutineIdsToday: Set<String>
        get() = routineLogs
            .filter { it.dateKey == todayDateKey && it.completed }
            .map { it.routineId }
            .toSet()

    val todayRoutines: List<RoutineItem>
        get() = routines.filter { it.appliesToDay(todayDayOfWeek) }

    val progressByLessonId: Map<String, LessonProgress>
        get() = lessonProgress.associateBy { it.lessonId }

    val completedLessonsCount: Int
        get() = lessonProgress.count { it.completed }

    val totalStars: Long
        get() = lessonProgress.sumOf { it.starsEarned } + routineLogs.filter { it.completed }.sumOf { it.starsEarned }

    val unlockedBadgeIds: Set<String>
        get() = buildSet {
            if (completedLessonsCount >= 1) add("first_lesson")
            if (routines.size >= 3) add("routine_builder")
            if (completedRoutineIdsToday.size >= 3) add("daily_checker")
            if (totalStars >= 50L) add("star_collector")
            if (lessonProgress.any { it.completed && it.bestScore >= it.totalQuestions }) add("quiz_master")
            if (completedLessonsCount >= 5) add("holiday_scholar")
        }
}

class SunnyLearnViewModel(
    private val repository: SunnyLearnRepository,
    private val currentUserId: String,
) : ViewModel() {

    private val _selectedDayOfWeek = MutableStateFlow(currentIsoDayOfWeek())
    val selectedDayOfWeek: StateFlow<Int> = _selectedDayOfWeek.asStateFlow()

    private val _showAllDays = MutableStateFlow(false)
    val showAllDays: StateFlow<Boolean> = _showAllDays.asStateFlow()

    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage.asStateFlow()

    private val _isBusy = MutableStateFlow(false)
    val isBusy: StateFlow<Boolean> = _isBusy.asStateFlow()

    val dashboardState: StateFlow<UiState<SunnyDashboardData>> = combine(
        repository.observeRoutines(currentUserId),
        repository.observeRoutineLogs(currentUserId),
        repository.observeLessonProgress(currentUserId),
    ) { routines, logs, progress ->
        SunnyDashboardData(
            routines = routines,
            routineLogs = logs,
            lessonProgress = progress,
            todayDateKey = currentDateKey(),
            todayDayOfWeek = currentIsoDayOfWeek(),
        )
    }
        .map<SunnyDashboardData, UiState<SunnyDashboardData>> { UiState.Success(it) }
        .catch { error ->
            val firestoreError = generateSequence(error) { it.cause }
                .filterIsInstance<FirebaseFirestoreException>()
                .firstOrNull()
            if (firestoreError == null && error is CancellationException) throw error
            val rootError = firestoreError ?: error
            Log.w(TAG, "Error observing dashboard state", rootError)
            emit(UiState.Error(rootError.localizedMessage ?: "Failed to load your holiday data."))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = UiState.Loading,
        )

    fun selectDay(isoDay: Int) {
        _showAllDays.value = false
        _selectedDayOfWeek.value = isoDay.coerceIn(1, 7)
    }

    fun selectAllDays() {
        _showAllDays.value = true
    }

    fun clearStatusMessage() {
        _statusMessage.value = null
    }

    fun saveRoutine(item: RoutineItem, isEdit: Boolean) {
        viewModelScope.launch {
            _isBusy.value = true
            val result = if (isEdit) {
                repository.updateRoutine(currentUserId, item)
            } else {
                repository.addRoutine(currentUserId, item)
            }
            _isBusy.value = false
            result
                .onSuccess {
                    _statusMessage.value = if (isEdit) "Routine updated!" else "Added to your timetable!"
                }
                .onFailure { error ->
                    _statusMessage.value = error.localizedMessage ?: "Could not save routine."
                }
        }
    }

    fun deleteRoutine(routineId: String) {
        viewModelScope.launch {
            _isBusy.value = true
            val result = repository.deleteRoutine(currentUserId, routineId)
            _isBusy.value = false
            result
                .onSuccess { _statusMessage.value = "Removed from timetable." }
                .onFailure { error ->
                    _statusMessage.value = error.localizedMessage ?: "Could not delete routine."
                }
        }
    }

    fun loadStarterRoutine() {
        viewModelScope.launch {
            _isBusy.value = true
            val result = repository.loadStarterRoutines(currentUserId)
            _isBusy.value = false
            result
                .onSuccess {
                    _statusMessage.value = "Loaded ${CurriculumCatalog.starterHolidayRoutines.size} sunny holiday activities!"
                }
                .onFailure { error ->
                    _statusMessage.value = error.localizedMessage ?: "Could not load starter timetable."
                }
        }
    }

    fun toggleRoutineDone(routineId: String, currentlyDone: Boolean) {
        viewModelScope.launch {
            val dateKey = currentDateKey()
            val result = repository.toggleRoutineCompletion(
                userId = currentUserId,
                routineId = routineId,
                dateKey = dateKey,
                completed = !currentlyDone,
            )
            result
                .onSuccess {
                    if (!currentlyDone) {
                        _statusMessage.value = "Great job! +5 stars earned!"
                    }
                }
                .onFailure { error ->
                    _statusMessage.value = error.localizedMessage ?: "Could not update check-in."
                }
        }
    }

    fun recordLessonResult(subjectId: String, lessonId: String, score: Int, totalQuestions: Int) {
        viewModelScope.launch {
            val result = repository.saveLessonCompletion(
                userId = currentUserId,
                subjectId = subjectId,
                lessonId = lessonId,
                score = score,
                totalQuestions = totalQuestions,
            )
            result
                .onSuccess {
                    val stars = 10 + score * 5
                    _statusMessage.value = "Lesson complete! Earned $stars stars!"
                }
                .onFailure { error ->
                    _statusMessage.value = error.localizedMessage ?: "Could not save lesson progress."
                }
        }
    }

    companion object {
        private const val TAG = "SunnyLearnVM"
        private const val STOP_TIMEOUT_MILLIS = 5000L

        fun currentDateKey(): String {
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            return formatter.format(Calendar.getInstance().time)
        }

        fun currentIsoDayOfWeek(): Int {
            val calDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
            return when (calDay) {
                Calendar.MONDAY -> 1
                Calendar.TUESDAY -> 2
                Calendar.WEDNESDAY -> 3
                Calendar.THURSDAY -> 4
                Calendar.FRIDAY -> 5
                Calendar.SATURDAY -> 6
                Calendar.SUNDAY -> 7
                else -> 1
            }
        }
    }
}

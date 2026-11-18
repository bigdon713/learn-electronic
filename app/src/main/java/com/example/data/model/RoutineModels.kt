package com.example.data.model

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

enum class RoutineCategory(val label: String) {
    STUDY("Study"),
    READING("Reading"),
    CREATIVE("Creative"),
    PLAY("Play"),
    CHORES("Chores"),
    REST("Meal & Rest");

    companion object {
        fun fromRaw(raw: String?): RoutineCategory =
            entries.firstOrNull { it.name == raw } ?: STUDY
    }
}

@Keep
@IgnoreExtraProperties
data class RoutineItem(
    @DocumentId val id: String = "",
    val userId: String = "",
    val title: String = "",
    val category: String = RoutineCategory.STUDY.name,
    val subjectId: String = "",
    val startMinutes: Long = 540L,
    val durationMinutes: Long = 30L,
    val daysOfWeek: List<Long> = listOf(1L, 2L, 3L, 4L, 5L, 6L, 7L),
    val note: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null,
) {
    fun categoryEnum(): RoutineCategory = RoutineCategory.fromRaw(category)

    fun appliesToDay(isoDayOfWeek: Int): Boolean =
        daysOfWeek.contains(isoDayOfWeek.toLong())

    fun formattedTimeRange(): String =
        "${formatMinutes(startMinutes)} • ${durationMinutes}m"

    fun toCreateMap(uid: String): Map<String, Any> = mapOf(
        "userId" to uid,
        "title" to title.trim().take(60),
        "category" to RoutineCategory.fromRaw(category).name,
        "subjectId" to subjectId.trim().take(40),
        "startMinutes" to startMinutes.coerceIn(0L, 1439L),
        "durationMinutes" to durationMinutes.coerceIn(5L, 480L),
        "daysOfWeek" to daysOfWeek.distinct().filter { it in 1L..7L }.ifEmpty { listOf(1L) },
        "note" to note.trim().take(160),
        "createdAt" to FieldValue.serverTimestamp(),
        "updatedAt" to FieldValue.serverTimestamp(),
    )

    fun toUpdateMap(): Map<String, Any> = mapOf(
        "title" to title.trim().take(60),
        "category" to RoutineCategory.fromRaw(category).name,
        "subjectId" to subjectId.trim().take(40),
        "startMinutes" to startMinutes.coerceIn(0L, 1439L),
        "durationMinutes" to durationMinutes.coerceIn(5L, 480L),
        "daysOfWeek" to daysOfWeek.distinct().filter { it in 1L..7L }.ifEmpty { listOf(1L) },
        "note" to note.trim().take(160),
        "updatedAt" to FieldValue.serverTimestamp(),
    )
}

@Keep
@IgnoreExtraProperties
data class RoutineLog(
    @DocumentId val id: String = "",
    val userId: String = "",
    val routineId: String = "",
    val dateKey: String = "",
    val completed: Boolean = false,
    val starsEarned: Long = 0L,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null,
)

@Keep
@IgnoreExtraProperties
data class LessonProgress(
    @DocumentId val id: String = "",
    val userId: String = "",
    val subjectId: String = "",
    val lessonId: String = "",
    val completed: Boolean = false,
    val bestScore: Long = 0L,
    val totalQuestions: Long = 3L,
    val starsEarned: Long = 0L,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null,
)

fun formatMinutes(totalMinutes: Long): String {
    val safe = totalMinutes.coerceIn(0L, 1439L).toInt()
    val hour24 = safe / 60
    val minute = safe % 60
    val amPm = if (hour24 < 12) "AM" else "PM"
    val hour12 = when {
        hour24 == 0 -> 12
        hour24 > 12 -> hour24 - 12
        else -> hour24
    }
    val minStr = minute.toString().padStart(2, '0')
    return "$hour12:$minStr $amPm"
}

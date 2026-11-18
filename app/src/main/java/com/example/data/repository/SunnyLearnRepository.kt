package com.example.data.repository

import android.content.Context
import com.example.R
import com.example.data.model.CurriculumCatalog
import com.example.data.model.LessonProgress
import com.example.data.model.RoutineCategory
import com.example.data.model.RoutineItem
import com.example.data.model.RoutineLog
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class SunnyLearnRepository(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth = Firebase.auth,
) {
    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private fun requireUserId(expectedUserId: String? = null): String {
        val uid = auth.currentUser?.uid
            ?: throw IllegalStateException("Please sign in with Google before saving changes.")
        if (expectedUserId != null && uid != expectedUserId) {
            throw IllegalStateException("Signed-in user changed; refusing cross-user write.")
        }
        return uid
    }

    fun observeRoutines(userId: String): Flow<List<RoutineItem>> = flow {
        val uid = requireUserId(userId)
        val path = "users/$uid/routines"
        db.collection("users")
            .document(uid)
            .collection("routines")
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects(
                    RoutineItem::class.java,
                    DocumentSnapshot.ServerTimestampBehavior.ESTIMATE,
                ).sortedBy { it.startMinutes }
            }
            .catch { error ->
                val firestoreError = generateSequence(error) { it.cause }
                    .filterIsInstance<FirebaseFirestoreException>()
                    .firstOrNull()
                if (firestoreError == null && error is CancellationException) throw error
                val exceptionToLog = firestoreError ?: (error as? Exception)
                if (exceptionToLog != null && auth.currentUser?.uid == uid) {
                    handleFirestoreError(exceptionToLog, OperationType.LIST, path)
                }
                throw error
            }
            .collect { emit(it) }
    }

    fun observeRoutineLogs(userId: String): Flow<List<RoutineLog>> = flow {
        val uid = requireUserId(userId)
        val path = "users/$uid/routineLogs"
        db.collection("users")
            .document(uid)
            .collection("routineLogs")
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects(
                    RoutineLog::class.java,
                    DocumentSnapshot.ServerTimestampBehavior.ESTIMATE,
                )
            }
            .catch { error ->
                val firestoreError = generateSequence(error) { it.cause }
                    .filterIsInstance<FirebaseFirestoreException>()
                    .firstOrNull()
                if (firestoreError == null && error is CancellationException) throw error
                val exceptionToLog = firestoreError ?: (error as? Exception)
                if (exceptionToLog != null && auth.currentUser?.uid == uid) {
                    handleFirestoreError(exceptionToLog, OperationType.LIST, path)
                }
                throw error
            }
            .collect { emit(it) }
    }

    fun observeLessonProgress(userId: String): Flow<List<LessonProgress>> = flow {
        val uid = requireUserId(userId)
        val path = "users/$uid/lessonProgress"
        db.collection("users")
            .document(uid)
            .collection("lessonProgress")
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects(
                    LessonProgress::class.java,
                    DocumentSnapshot.ServerTimestampBehavior.ESTIMATE,
                )
            }
            .catch { error ->
                val firestoreError = generateSequence(error) { it.cause }
                    .filterIsInstance<FirebaseFirestoreException>()
                    .firstOrNull()
                if (firestoreError == null && error is CancellationException) throw error
                val exceptionToLog = firestoreError ?: (error as? Exception)
                if (exceptionToLog != null && auth.currentUser?.uid == uid) {
                    handleFirestoreError(exceptionToLog, OperationType.LIST, path)
                }
                throw error
            }
            .collect { emit(it) }
    }

    suspend fun addRoutine(userId: String, item: RoutineItem): Result<Unit> {
        var docPath = "users/$userId/routines"
        return try {
            val uid = requireUserId(userId)
            val cleanTitle = item.title.trim()
            require(cleanTitle.isNotEmpty() && cleanTitle.length <= 60) {
                "Activity title must be between 1 and 60 characters."
            }
            val docRef = db.collection("users").document(uid).collection("routines").document()
            docPath = docRef.path
            docRef.set(item.toCreateMap(uid)).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docPath)
            Result.failure(e)
        }
    }

    suspend fun updateRoutine(userId: String, item: RoutineItem): Result<Unit> {
        var docPath = "users/$userId/routines/${item.id}"
        return try {
            val uid = requireUserId(userId)
            require(item.id.isNotBlank()) { "Routine ID is missing." }
            val cleanTitle = item.title.trim()
            require(cleanTitle.isNotEmpty() && cleanTitle.length <= 60) {
                "Activity title must be between 1 and 60 characters."
            }
            val docRef = db.collection("users").document(uid).collection("routines").document(item.id)
            docPath = docRef.path
            docRef.update(item.toUpdateMap()).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docPath)
            Result.failure(e)
        }
    }

    suspend fun deleteRoutine(userId: String, routineId: String): Result<Unit> {
        var docPath = "users/$userId/routines/$routineId"
        return try {
            val uid = requireUserId(userId)
            require(routineId.isNotBlank()) { "Routine ID is missing." }
            val docRef = db.collection("users").document(uid).collection("routines").document(routineId)
            docPath = docRef.path
            docRef.delete().await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.DELETE, docPath)
            Result.failure(e)
        }
    }

    suspend fun loadStarterRoutines(userId: String): Result<Unit> {
        val batchPath = "users/$userId/routines"
        return try {
            val uid = requireUserId(userId)
            val collection = db.collection("users").document(uid).collection("routines")
            val batch = db.batch()
            CurriculumCatalog.starterHolidayRoutines.forEach { template ->
                val docRef = collection.document()
                val item = RoutineItem(
                    userId = uid,
                    title = template.title,
                    category = template.category.name,
                    subjectId = template.subjectId,
                    startMinutes = template.startMinutes,
                    durationMinutes = template.durationMinutes,
                    daysOfWeek = listOf(1L, 2L, 3L, 4L, 5L, 6L, 7L),
                    note = template.note,
                )
                batch.set(docRef, item.toCreateMap(uid))
            }
            batch.commit().await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, batchPath)
            Result.failure(e)
        }
    }

    suspend fun toggleRoutineCompletion(
        userId: String,
        routineId: String,
        dateKey: String,
        completed: Boolean,
    ): Result<Unit> {
        val logId = "${dateKey}_${routineId}"
        var docPath = "users/$userId/routineLogs/$logId"
        return try {
            val uid = requireUserId(userId)
            require(routineId.isNotBlank()) { "Routine ID cannot be blank." }
            require(dateKey.length == 10) { "Invalid date key." }
            val docRef = db.collection("users").document(uid).collection("routineLogs").document(logId)
            docPath = docRef.path
            val existing = docRef.get().await()
            val stars = if (completed) 5L else 0L
            if (existing.exists()) {
                docRef.update(
                    mapOf(
                        "completed" to completed,
                        "starsEarned" to stars,
                        "updatedAt" to FieldValue.serverTimestamp(),
                    )
                ).await()
            } else {
                docRef.set(
                    mapOf(
                        "userId" to uid,
                        "routineId" to routineId,
                        "dateKey" to dateKey,
                        "completed" to completed,
                        "starsEarned" to stars,
                        "createdAt" to FieldValue.serverTimestamp(),
                        "updatedAt" to FieldValue.serverTimestamp(),
                    )
                ).await()
            }
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, docPath)
            Result.failure(e)
        }
    }

    suspend fun saveLessonCompletion(
        userId: String,
        subjectId: String,
        lessonId: String,
        score: Int,
        totalQuestions: Int,
    ): Result<Unit> {
        var docPath = "users/$userId/lessonProgress/$lessonId"
        return try {
            val uid = requireUserId(userId)
            require(subjectId.isNotBlank() && lessonId.isNotBlank()) { "Invalid lesson reference." }
            val safeTotal = totalQuestions.coerceIn(1, 10).toLong()
            val safeScore = score.coerceIn(0, safeTotal.toInt()).toLong()
            val earnedStars = (10L + safeScore * 5L).coerceAtMost(50L)

            val docRef = db.collection("users").document(uid).collection("lessonProgress").document(lessonId)
            docPath = docRef.path
            val existing = docRef.get().await()
            if (existing.exists()) {
                val prevBest = existing.getLong("bestScore") ?: 0L
                val prevStars = existing.getLong("starsEarned") ?: 0L
                docRef.update(
                    mapOf(
                        "completed" to true,
                        "bestScore" to maxOf(prevBest, safeScore),
                        "totalQuestions" to safeTotal,
                        "starsEarned" to maxOf(prevStars, earnedStars),
                        "updatedAt" to FieldValue.serverTimestamp(),
                    )
                ).await()
            } else {
                docRef.set(
                    mapOf(
                        "userId" to uid,
                        "subjectId" to subjectId,
                        "lessonId" to lessonId,
                        "completed" to true,
                        "bestScore" to safeScore,
                        "totalQuestions" to safeTotal,
                        "starsEarned" to earnedStars,
                        "createdAt" to FieldValue.serverTimestamp(),
                        "updatedAt" to FieldValue.serverTimestamp(),
                    )
                ).await()
            }
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, docPath)
            Result.failure(e)
        }
    }
}

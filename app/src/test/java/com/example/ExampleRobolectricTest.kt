package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.CurriculumCatalog
import com.example.data.model.RoutineCategory
import com.example.data.model.RoutineItem
import com.example.data.model.formatMinutes
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read app name from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("SunnyLearn", appName)
  }

  @Test
  fun `curriculum catalog has six subjects with lessons and quizzes`() {
    assertEquals(6, CurriculumCatalog.subjects.size)
    CurriculumCatalog.subjects.forEach { subject ->
      assertEquals(3, subject.lessons.size)
      subject.lessons.forEach { lesson ->
        assertTrue(lesson.quiz.isNotEmpty())
      }
    }
  }

  @Test
  fun `routine item formats time and creates valid firestore payload`() {
    val item = RoutineItem(
      id = "r_1",
      userId = "user_1",
      title = "Morning Math",
      category = RoutineCategory.STUDY.name,
      subjectId = "math",
      startMinutes = 570L,
      durationMinutes = 30L,
      daysOfWeek = listOf(1L, 3L, 5L),
      note = "Solve 3 problems",
    )
    assertEquals("9:30 AM", formatMinutes(item.startMinutes))
    val payload = item.toCreateMap("user_1")
    assertTrue(!payload.containsKey("id"))
    assertEquals("Morning Math", payload["title"])
    assertTrue(item.appliesToDay(1))
    assertTrue(!item.appliesToDay(2))
  }
}

package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.model.CurriculumCatalog
import com.example.data.model.RoutineItem
import com.example.data.model.Subject
import com.example.data.model.formatMinutes
import com.example.ui.components.CategoryBadge
import com.example.ui.components.StarCounterPill
import com.example.ui.components.isoDayFullName
import com.example.ui.viewmodel.SunnyDashboardData
import java.util.Calendar

@Composable
fun TodayScreen(
    explorerName: String,
    dashboard: SunnyDashboardData,
    onToggleRoutineDone: (routineId: String, currentlyDone: Boolean) -> Unit,
    onOpenRoutineTab: () -> Unit,
    onOpenSubjectsTab: () -> Unit,
    onSelectSubject: (Subject) -> Unit,
    onLoadStarterRoutine: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val todayRoutines = dashboard.todayRoutines
    val completedTodayIds = dashboard.completedRoutineIdsToday
    val doneTodayCount = todayRoutines.count { completedTodayIds.contains(it.id) }
    val routineFraction = if (todayRoutines.isEmpty()) 0f else doneTodayCount.toFloat() / todayRoutines.size
    val totalLessons = CurriculumCatalog.subjects.sumOf { it.lessons.size }
    val dailyTip = remember(dashboard.todayDayOfWeek) {
        CurriculumCatalog.holidayTips[dashboard.todayDayOfWeek % CurriculumCatalog.holidayTips.size]
    }
    val greeting = remember {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        when {
            hour < 12 -> "Good morning"
            hour < 17 -> "Good afternoon"
            else -> "Good evening"
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        LazyColumn(
            modifier = Modifier
                .widthIn(max = 720.dp)
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            // Top Greeting & Hero Banner
            item {
                Card(
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(165.dp),
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_holiday_hero),
                                contentDescription = "Sunny holiday study desk banner",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize(),
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                Color.Black.copy(alpha = 0.55f),
                                            )
                                        )
                                    )
                            )
                            Row(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(14.dp),
                            ) {
                                StarCounterPill(stars = dashboard.totalStars)
                            }
                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(16.dp),
                            ) {
                                Text(
                                    text = "$greeting, ${explorerName.ifBlank { "Explorer" }}!",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = Color.White,
                                )
                                Text(
                                    text = "${isoDayFullName(dashboard.todayDayOfWeek)} Holiday Quest",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White.copy(alpha = 0.9f),
                                )
                            }
                        }

                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "Today's Routine Progress",
                                    style = MaterialTheme.typography.titleSmall,
                                )
                                Text(
                                    text = "$doneTodayCount / ${todayRoutines.size} done",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                            }
                            LinearProgressIndicator(
                                progress = { routineFraction },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(50)),
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text(
                                    text = "Lessons finished: ${dashboard.completedLessonsCount} / $totalLessons",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                                Text(
                                    text = "${dashboard.unlockedBadgeIds.size} badges",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.secondary,
                                )
                            }
                        }
                    }
                }
            }

            // Today's Timetable Preview
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.CalendarToday,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            text = "Today's Timetable",
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                    TextButton(
                        onClick = onOpenRoutineTab,
                        modifier = Modifier.testTag("today_view_all_routine_button"),
                    ) {
                        Text("Edit All")
                    }
                }
            }

            if (todayRoutines.isEmpty()) {
                item {
                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = 1.dp,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            Text(
                                text = "No activities scheduled for ${isoDayFullName(dashboard.todayDayOfWeek)} yet!",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                            Text(
                                text = "Load our balanced sunny holiday timetable or create your own schedule.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                            ) {
                                Button(
                                    onClick = onLoadStarterRoutine,
                                    modifier = Modifier
                                        .minimumInteractiveComponentSize()
                                        .testTag("today_load_starter_button"),
                                ) {
                                    Icon(Icons.Rounded.AutoAwesome, contentDescription = null)
                                    Spacer(Modifier.width(6.dp))
                                    Text("Load Starter")
                                }
                                OutlinedButton(
                                    onClick = onOpenRoutineTab,
                                    modifier = Modifier
                                        .minimumInteractiveComponentSize()
                                        .testTag("today_create_routine_button"),
                                ) {
                                    Text("Build Own")
                                }
                            }
                        }
                    }
                }
            } else {
                items(todayRoutines.take(4), key = { it.id }) { item ->
                    val isDone = completedTodayIds.contains(item.id)
                    val linkedSubject = if (item.subjectId.isNotEmpty()) {
                        CurriculumCatalog.getSubject(item.subjectId)
                    } else null

                    TodayRoutineQuickCard(
                        item = item,
                        isDone = isDone,
                        linkedSubject = linkedSubject,
                        onToggleDone = { onToggleRoutineDone(item.id, isDone) },
                        onOpenSubject = { linkedSubject?.let(onSelectSubject) },
                    )
                }
            }

            // Holiday Subject Picks
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.MenuBook,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                        )
                        Text(
                            text = "Explore Holiday Subjects",
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                    TextButton(
                        onClick = onOpenSubjectsTab,
                        modifier = Modifier.testTag("today_all_subjects_button"),
                    ) {
                        Text("See All 6")
                    }
                }
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(CurriculumCatalog.subjects, key = { it.id }) { subject ->
                        val completedCount = subject.lessons.count { lesson ->
                            dashboard.progressByLessonId[lesson.id]?.completed == true
                        }
                        Surface(
                            shape = RoundedCornerShape(24.dp),
                            color = subject.containerColor,
                            modifier = Modifier
                                .width(172.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .clickable { onSelectSubject(subject) }
                                .testTag("today_subject_card_${subject.id}"),
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = subject.accentColor,
                                    modifier = Modifier.size(42.dp),
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = subject.icon,
                                            contentDescription = subject.title,
                                            tint = Color.White,
                                        )
                                    }
                                }
                                Text(
                                    text = subject.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color(0xFF22223B),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                Text(
                                    text = "$completedCount / ${subject.lessons.size} lessons",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = subject.accentColor,
                                )
                            }
                        }
                    }
                }
            }

            // Daily Explorer Tip
            item {
                Surface(
                    shape = RoundedCornerShape(22.dp),
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Lightbulb,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.size(28.dp),
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = "Holiday Explorer Tip",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                            )
                            Text(
                                text = dailyTip,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(12.dp)) }
        }
    }
}

@Composable
private fun TodayRoutineQuickCard(
    item: RoutineItem,
    isDone: Boolean,
    linkedSubject: Subject?,
    onToggleDone: () -> Unit,
    onOpenSubject: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = if (isDone) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
        else MaterialTheme.colorScheme.surface,
        tonalElevation = if (isDone) 0.dp else 2.dp,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            IconButton(
                onClick = onToggleDone,
                modifier = Modifier
                    .minimumInteractiveComponentSize()
                    .testTag("today_check_routine_${item.id}"),
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(
                            if (isDone) MaterialTheme.colorScheme.tertiary
                            else MaterialTheme.colorScheme.surfaceVariant
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    if (isDone) {
                        Icon(
                            imageVector = Icons.Rounded.Check,
                            contentDescription = "Completed",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp),
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "${formatMinutes(item.startMinutes)} • ${item.durationMinutes}m",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    CategoryBadge(category = item.categoryEnum())
                }
            }

            if (linkedSubject != null) {
                IconButton(
                    onClick = onOpenSubject,
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("today_launch_subject_${item.id}"),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                        contentDescription = "Open ${linkedSubject.title}",
                        tint = linkedSubject.accentColor,
                    )
                }
            }
        }
    }
}

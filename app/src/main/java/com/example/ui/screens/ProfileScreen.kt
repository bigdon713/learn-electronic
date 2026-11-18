package com.example.ui.screens

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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.model.CurriculumCatalog
import com.example.ui.theme.MeadowEmerald
import com.example.ui.theme.SkyCerulean
import com.example.ui.theme.SunburstGold
import com.example.ui.viewmodel.SunnyDashboardData

@Composable
fun ProfileScreen(
    explorerName: String,
    explorerEmail: String,
    dashboard: SunnyDashboardData,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val totalLessons = CurriculumCatalog.subjects.sumOf { it.lessons.size }
    val unlockedIds = dashboard.unlockedBadgeIds

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        LazyColumn(
            modifier = Modifier
                .widthIn(max = 720.dp)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                Surface(
                    shape = RoundedCornerShape(28.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(64.dp),
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = explorerName.firstOrNull()?.uppercase() ?: "E",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = Color.White,
                                )
                            }
                        }
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            Text(
                                text = explorerName.ifBlank { "Holiday Explorer" },
                                style = MaterialTheme.typography.headlineSmall,
                            )
                            if (explorerEmail.isNotBlank()) {
                                Text(
                                    text = explorerEmail,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            Text(
                                text = "${unlockedIds.size} of ${CurriculumCatalog.badges.size} Badges Unlocked",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                }
            }

            // Summary Stat Cards
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    StatTile(
                        icon = Icons.Rounded.Star,
                        value = "${dashboard.totalStars}",
                        label = "Stars",
                        tint = SunburstGold,
                        modifier = Modifier.weight(1f),
                    )
                    StatTile(
                        icon = Icons.Rounded.MenuBook,
                        value = "${dashboard.completedLessonsCount}/$totalLessons",
                        label = "Lessons",
                        tint = SkyCerulean,
                        modifier = Modifier.weight(1f),
                    )
                    StatTile(
                        icon = Icons.Rounded.Schedule,
                        value = "${dashboard.completedRoutineIdsToday.size}",
                        label = "Done Today",
                        tint = MeadowEmerald,
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            // Explorer Badges
            item {
                Text(
                    text = "Explorer Badges",
                    style = MaterialTheme.typography.titleLarge,
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    CurriculumCatalog.badges.forEach { badge ->
                        val unlocked = unlockedIds.contains(badge.id)
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (unlocked) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surface,
                            tonalElevation = 1.dp,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = if (unlocked) SunburstGold else MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier.size(44.dp),
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = if (unlocked) Icons.Rounded.EmojiEvents else Icons.Rounded.Lock,
                                            contentDescription = if (unlocked) "Unlocked badge" else "Locked badge",
                                            tint = if (unlocked) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                        )
                                    }
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = badge.title,
                                        style = MaterialTheme.typography.titleMedium,
                                    )
                                    Text(
                                        text = badge.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(50),
                                    color = if (unlocked) MeadowEmerald else MaterialTheme.colorScheme.surfaceVariant,
                                ) {
                                    Text(
                                        text = if (unlocked) "Unlocked" else badge.requirementLabel,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (unlocked) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Subject Progress Breakdown
            item {
                Text(
                    text = "Subject Mastery",
                    style = MaterialTheme.typography.titleLarge,
                )
            }

            item {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 1.dp,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        CurriculumCatalog.subjects.forEach { subject ->
                            val done = subject.lessons.count { lesson ->
                                dashboard.progressByLessonId[lesson.id]?.completed == true
                            }
                            val total = subject.lessons.size
                            val frac = if (total == 0) 0f else done.toFloat() / total
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    Text(
                                        text = subject.title,
                                        style = MaterialTheme.typography.titleSmall,
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    ) {
                                        if (done == total) {
                                            Icon(
                                                imageVector = Icons.Rounded.CheckCircle,
                                                contentDescription = "Subject mastered",
                                                tint = subject.accentColor,
                                                modifier = Modifier.size(16.dp),
                                            )
                                        }
                                        Text(
                                            text = "$done/$total",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = subject.accentColor,
                                        )
                                    }
                                }
                                LinearProgressIndicator(
                                    progress = { frac },
                                    color = subject.accentColor,
                                    trackColor = subject.containerColor,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(50)),
                                )
                            }
                        }
                    }
                }
            }

            item {
                OutlinedButton(
                    onClick = onSignOut,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .minimumInteractiveComponentSize()
                        .testTag("sign_out_button"),
                ) {
                    Icon(Icons.AutoMirrored.Rounded.Logout, contentDescription = null)
                    Spacer(Modifier.size(8.dp))
                    Text("Sign Out")
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun StatTile(
    icon: ImageVector,
    value: String,
    label: String,
    tint: Color,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = tint,
                modifier = Modifier.size(26.dp),
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.model.CurriculumCatalog
import com.example.data.model.RoutineItem
import com.example.data.model.Subject
import com.example.data.model.formatMinutes
import com.example.ui.components.AddEditRoutineDialog
import com.example.ui.components.CategoryBadge
import com.example.ui.components.isoDayFullName
import com.example.ui.components.isoDayShortName
import com.example.ui.viewmodel.SunnyDashboardData

@Composable
fun RoutineScreen(
    dashboard: SunnyDashboardData,
    selectedDayOfWeek: Int,
    showAllDays: Boolean,
    onSelectDay: (Int) -> Unit,
    onSelectAllDays: () -> Unit,
    onSaveRoutine: (RoutineItem, isEdit: Boolean) -> Unit,
    onDeleteRoutine: (String) -> Unit,
    onToggleRoutineDone: (routineId: String, currentlyDone: Boolean) -> Unit,
    onLoadStarterRoutine: () -> Unit,
    onSelectSubject: (Subject) -> Unit,
    modifier: Modifier = Modifier,
) {
    var editingItem by remember { mutableStateOf<RoutineItem?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }
    var pendingDeleteItem by remember { mutableStateOf<RoutineItem?>(null) }

    val filteredRoutines = remember(dashboard.routines, selectedDayOfWeek, showAllDays) {
        if (showAllDays) {
            dashboard.routines
        } else {
            dashboard.routines.filter { it.appliesToDay(selectedDayOfWeek) }
        }
    }

    val isViewingToday = !showAllDays && selectedDayOfWeek == dashboard.todayDayOfWeek
    val completedTodayIds = dashboard.completedRoutineIdsToday
    val doneCount = filteredRoutines.count { completedTodayIds.contains(it.id) }
    val progressFraction = if (filteredRoutines.isEmpty()) 0f else doneCount.toFloat() / filteredRoutines.size

    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter,
        ) {
            LazyColumn(
                modifier = Modifier
                    .widthIn(max = 760.dp)
                    .fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column {
                            Text(
                                text = "Daily Timetable",
                                style = MaterialTheme.typography.headlineMedium,
                            )
                            Text(
                                text = if (showAllDays) "Showing all weekly activities"
                                else "${isoDayFullName(selectedDayOfWeek)} Schedule",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        if (dashboard.routines.isEmpty()) {
                            TextButton(
                                onClick = onLoadStarterRoutine,
                                modifier = Modifier.testTag("header_load_starter_button"),
                            ) {
                                Text("Load Starter")
                            }
                        }
                    }
                }

                // Day-of-week filter strip
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items((1..7).toList()) { day ->
                            val isToday = day == dashboard.todayDayOfWeek
                            FilterChip(
                                selected = !showAllDays && selectedDayOfWeek == day,
                                onClick = { onSelectDay(day) },
                                label = {
                                    Text(if (isToday) "${isoDayShortName(day)} • Today" else isoDayShortName(day))
                                },
                                modifier = Modifier.testTag("routine_day_filter_$day"),
                            )
                        }
                        item {
                            FilterChip(
                                selected = showAllDays,
                                onClick = onSelectAllDays,
                                label = { Text("All Days") },
                                modifier = Modifier.testTag("routine_day_filter_all"),
                            )
                        }
                    }
                }

                // Today's Progress Summary Card
                if (filteredRoutines.isNotEmpty() && isViewingToday) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    Text(
                                        text = "Today's Checklist Progress",
                                        style = MaterialTheme.typography.titleSmall,
                                    )
                                    Text(
                                        text = "$doneCount / ${filteredRoutines.size} (+${doneCount * 5} ⭐)",
                                        style = MaterialTheme.typography.labelLarge,
                                    )
                                }
                                LinearProgressIndicator(
                                    progress = { progressFraction },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(50)),
                                )
                            }
                        }
                    }
                }

                if (filteredRoutines.isEmpty()) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(28.dp),
                            color = MaterialTheme.colorScheme.surface,
                            tonalElevation = 1.dp,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Column(
                                modifier = Modifier.padding(22.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.img_routine_empty),
                                    contentDescription = "Empty daily timetable illustration",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(170.dp)
                                        .clip(RoundedCornerShape(20.dp)),
                                )
                                Text(
                                    text = "Your timetable is wide open!",
                                    style = MaterialTheme.typography.titleLarge,
                                    textAlign = TextAlign.Center,
                                )
                                Text(
                                    text = "Add your own study, reading, and playtime blocks, or start with our sunny holiday routine.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center,
                                )
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                ) {
                                    Button(
                                        onClick = { showAddDialog = true },
                                        modifier = Modifier
                                            .minimumInteractiveComponentSize()
                                            .testTag("empty_add_routine_button"),
                                    ) {
                                        Icon(Icons.Rounded.Add, contentDescription = null)
                                        Spacer(Modifier.width(6.dp))
                                        Text("Add Activity")
                                    }
                                    if (dashboard.routines.isEmpty()) {
                                        OutlinedButton(
                                            onClick = onLoadStarterRoutine,
                                            modifier = Modifier
                                                .minimumInteractiveComponentSize()
                                                .testTag("empty_load_starter_button"),
                                        ) {
                                            Icon(Icons.Rounded.AutoAwesome, contentDescription = null)
                                            Spacer(Modifier.width(6.dp))
                                            Text("Starter Plan")
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    items(filteredRoutines, key = { it.id }) { item ->
                        val isDoneToday = completedTodayIds.contains(item.id)
                        val linkedSubject = if (item.subjectId.isNotEmpty()) {
                            CurriculumCatalog.getSubject(item.subjectId)
                        } else null

                        RoutineTimelineCard(
                            item = item,
                            canCheckOff = isViewingToday || showAllDays,
                            isDoneToday = isDoneToday,
                            linkedSubject = linkedSubject,
                            showDaysSummary = showAllDays,
                            onToggleDone = { onToggleRoutineDone(item.id, isDoneToday) },
                            onEdit = { editingItem = item },
                            onDelete = { pendingDeleteItem = item },
                            onOpenSubject = { linkedSubject?.let(onSelectSubject) },
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { showAddDialog = true },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("add_routine_fab"),
        ) {
            Icon(Icons.Rounded.Add, contentDescription = "Add daily timetable activity")
        }
    }

    if (showAddDialog) {
        AddEditRoutineDialog(
            initialItem = null,
            defaultDayOfWeek = selectedDayOfWeek,
            onDismiss = { showAddDialog = false },
            onSave = { newItem ->
                showAddDialog = false
                onSaveRoutine(newItem, false)
            },
        )
    }

    editingItem?.let { itemToEdit ->
        AddEditRoutineDialog(
            initialItem = itemToEdit,
            defaultDayOfWeek = selectedDayOfWeek,
            onDismiss = { editingItem = null },
            onSave = { updated ->
                editingItem = null
                onSaveRoutine(updated, true)
            },
        )
    }

    pendingDeleteItem?.let { itemToDelete ->
        AlertDialog(
            onDismissRequest = { pendingDeleteItem = null },
            title = { Text("Remove Activity?") },
            text = { Text("Remove \"${itemToDelete.title}\" from your daily timetable?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        pendingDeleteItem = null
                        onDeleteRoutine(itemToDelete.id)
                    },
                    modifier = Modifier.testTag("confirm_delete_routine_button"),
                ) {
                    Text("Remove", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingDeleteItem = null }) {
                    Text("Keep")
                }
            },
        )
    }
}

@Composable
private fun RoutineTimelineCard(
    item: RoutineItem,
    canCheckOff: Boolean,
    isDoneToday: Boolean,
    linkedSubject: Subject?,
    showDaysSummary: Boolean,
    onToggleDone: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onOpenSubject: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = if (isDoneToday && canCheckOff) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
        else MaterialTheme.colorScheme.surface,
        tonalElevation = if (isDoneToday && canCheckOff) 0.dp else 2.dp,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                if (canCheckOff) {
                    IconButton(
                        onClick = onToggleDone,
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("routine_check_${item.id}"),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isDoneToday) MaterialTheme.colorScheme.tertiary
                                    else MaterialTheme.colorScheme.surfaceVariant
                                ),
                            contentAlignment = Alignment.Center,
                        ) {
                            if (isDoneToday) {
                                Icon(
                                    imageVector = Icons.Rounded.Check,
                                    contentDescription = "Mark incomplete",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp),
                                )
                            }
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
                            textDecoration = if (isDoneToday && canCheckOff) TextDecoration.LineThrough
                            else TextDecoration.None
                        ),
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = "${formatMinutes(item.startMinutes)} • ${item.durationMinutes} min",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        CategoryBadge(category = item.categoryEnum())
                    }
                }

                IconButton(
                    onClick = onEdit,
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("routine_edit_${item.id}"),
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Edit,
                        contentDescription = "Edit ${item.title}",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("routine_delete_${item.id}"),
                ) {
                    Icon(
                        imageVector = Icons.Rounded.DeleteOutline,
                        contentDescription = "Delete ${item.title}",
                        tint = MaterialTheme.colorScheme.error,
                    )
                }
            }

            if (item.note.isNotBlank()) {
                Text(
                    text = item.note,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = if (canCheckOff) 48.dp else 4.dp),
                )
            }

            if (showDaysSummary) {
                val daysText = if (item.daysOfWeek.size == 7) "Every day"
                else item.daysOfWeek.sorted().joinToString(", ") { isoDayShortName(it.toInt()) }
                Text(
                    text = "Days: $daysText",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(start = if (canCheckOff) 48.dp else 4.dp),
                )
            }

            if (linkedSubject != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = if (canCheckOff) 44.dp else 0.dp),
                    horizontalArrangement = Arrangement.Start,
                ) {
                    TextButton(
                        onClick = onOpenSubject,
                        modifier = Modifier.testTag("routine_open_subject_${item.id}"),
                    ) {
                        Icon(
                            imageVector = linkedSubject.icon,
                            contentDescription = null,
                            tint = linkedSubject.accentColor,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "Open ${linkedSubject.title}",
                            color = linkedSubject.accentColor,
                        )
                        Spacer(Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                            contentDescription = null,
                            tint = linkedSubject.accentColor,
                            modifier = Modifier.size(16.dp),
                        )
                    }
                }
            }
        }
    }
}

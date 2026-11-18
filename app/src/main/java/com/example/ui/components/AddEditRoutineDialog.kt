package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.CurriculumCatalog
import com.example.data.model.RoutineCategory
import com.example.data.model.RoutineItem
import com.example.data.model.formatMinutes

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddEditRoutineDialog(
    initialItem: RoutineItem?,
    defaultDayOfWeek: Int,
    onDismiss: () -> Unit,
    onSave: (RoutineItem) -> Unit,
) {
    var title by remember(initialItem) { mutableStateOf(initialItem?.title ?: "") }
    var category by remember(initialItem) {
        mutableStateOf(initialItem?.categoryEnum() ?: RoutineCategory.STUDY)
    }
    var subjectId by remember(initialItem) { mutableStateOf(initialItem?.subjectId ?: "") }
    var startMinutes by remember(initialItem) {
        mutableIntStateOf((initialItem?.startMinutes ?: 540L).toInt())
    }
    var durationMinutes by remember(initialItem) {
        mutableIntStateOf((initialItem?.durationMinutes ?: 30L).toInt())
    }
    var selectedDays by remember(initialItem) {
        mutableStateOf(
            initialItem?.daysOfWeek?.map { it.toInt() }?.toSet()
                ?: setOf(1, 2, 3, 4, 5, 6, 7)
        )
    }
    var note by remember(initialItem) { mutableStateOf(initialItem?.note ?: "") }
    var errorText by remember { mutableStateOf<String?>(null) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .widthIn(max = 560.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Text(
                    text = if (initialItem == null) "Add Daily Activity" else "Edit Activity",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        if (it.length <= 60) {
                            title = it
                            errorText = null
                        }
                    },
                    label = { Text("Activity Name") },
                    placeholder = { Text("e.g., Morning Math Puzzles") },
                    singleLine = true,
                    supportingText = { Text("${title.length}/60") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("routine_title_input"),
                )

                Text(
                    text = "Category",
                    style = MaterialTheme.typography.titleSmall,
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    RoutineCategory.entries.forEach { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(cat.label) },
                            leadingIcon = {
                                Icon(
                                    imageVector = cat.icon(),
                                    contentDescription = null,
                                )
                            },
                            modifier = Modifier.testTag("category_chip_${cat.name.lowercase()}"),
                        )
                    }
                }

                // Start Time Selector
                Text(
                    text = "Start Time",
                    style = MaterialTheme.typography.titleSmall,
                )
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        IconButton(
                            onClick = {
                                startMinutes = (startMinutes - 15).coerceAtLeast(0)
                            },
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .testTag("time_minus_button"),
                        ) {
                            Icon(Icons.Rounded.Remove, contentDescription = "15 minutes earlier")
                        }
                        Text(
                            text = formatMinutes(startMinutes.toLong()),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        IconButton(
                            onClick = {
                                startMinutes = (startMinutes + 15).coerceAtMost(1425)
                            },
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .testTag("time_plus_button"),
                        ) {
                            Icon(Icons.Rounded.Add, contentDescription = "15 minutes later")
                        }
                    }
                }

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    listOf(
                        "8:30 AM" to 510,
                        "10:00 AM" to 600,
                        "2:00 PM" to 840,
                        "4:30 PM" to 990,
                        "7:30 PM" to 1170,
                    ).forEach { (label, mins) ->
                        FilterChip(
                            selected = startMinutes == mins,
                            onClick = { startMinutes = mins },
                            label = { Text(label) },
                        )
                    }
                }

                // Duration Selector
                Text(
                    text = "Duration",
                    style = MaterialTheme.typography.titleSmall,
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    listOf(15, 30, 45, 60, 90).forEach { mins ->
                        FilterChip(
                            selected = durationMinutes == mins,
                            onClick = { durationMinutes = mins },
                            label = { Text("${mins}m") },
                            modifier = Modifier.testTag("duration_chip_$mins"),
                        )
                    }
                }

                // Optional Subject Link
                Text(
                    text = "Link Holiday Subject (Optional)",
                    style = MaterialTheme.typography.titleSmall,
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    FilterChip(
                        selected = subjectId.isEmpty(),
                        onClick = { subjectId = "" },
                        label = { Text("None") },
                    )
                    CurriculumCatalog.subjects.forEach { subj ->
                        FilterChip(
                            selected = subjectId == subj.id,
                            onClick = { subjectId = subj.id },
                            label = { Text(subj.title) },
                        )
                    }
                }

                // Days of Week
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Repeat Days",
                        style = MaterialTheme.typography.titleSmall,
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        TextButton(onClick = { selectedDays = (1..7).toSet() }) {
                            Text("Every day")
                        }
                        TextButton(onClick = { selectedDays = (1..5).toSet() }) {
                            Text("Weekdays")
                        }
                    }
                }
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    (1..7).forEach { day ->
                        val isSelected = selectedDays.contains(day)
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                selectedDays = if (isSelected && selectedDays.size > 1) {
                                    selectedDays - day
                                } else {
                                    selectedDays + day
                                }
                            },
                            label = { Text(isoDayShortName(day)) },
                            modifier = Modifier.testTag("day_chip_$day"),
                        )
                    }
                }

                OutlinedTextField(
                    value = note,
                    onValueChange = { if (it.length <= 160) note = it },
                    label = { Text("Goal or Reminder (Optional)") },
                    placeholder = { Text("e.g., Finish 1 lesson & quiz") },
                    maxLines = 2,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("routine_note_input"),
                )

                errorText?.let { err ->
                    Text(
                        text = err,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End),
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("cancel_routine_button"),
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            val trimmed = title.trim()
                            if (trimmed.isEmpty()) {
                                errorText = "Please enter an activity name."
                                return@Button
                            }
                            val finalDays = selectedDays.ifEmpty { setOf(defaultDayOfWeek) }
                                .sorted()
                                .map { it.toLong() }
                            val updated = (initialItem ?: RoutineItem()).copy(
                                title = trimmed,
                                category = category.name,
                                subjectId = subjectId,
                                startMinutes = startMinutes.toLong(),
                                durationMinutes = durationMinutes.toLong(),
                                daysOfWeek = finalDays,
                                note = note.trim(),
                            )
                            onSave(updated)
                        },
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("save_routine_button"),
                    ) {
                        Text(if (initialItem == null) "Add Activity" else "Save Changes")
                    }
                }
            }
        }
    }
}

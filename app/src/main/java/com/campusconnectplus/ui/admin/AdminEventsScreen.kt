package com.campusconnectplus.ui.admin

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.campusconnectplus.core.ui.components.FloatingScrollbar
import com.campusconnectplus.feature_admin.events.AdminEventsViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AdminEventsScreen(vm: AdminEventsViewModel) {
    val state = rememberLazyListState()
    val events by vm.events.collectAsState()
    var showCreate by remember { mutableStateOf(false) }

    CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
        Box(Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxSize()) {
                TopBar(title = "Manage Events", subtitle = "${events.size} events stored locally") {
                    showCreate = true
                }

                Spacer(Modifier.height(10.dp))

                if (events.isEmpty()) {
                    EmptyAdminPanel(
                        iconText = "📅",
                        title = "No events created yet",
                        hint = "Click “Add” to create your first event."
                    )
                } else {
                    LazyColumn(
                        state = state,
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 84.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(events.size) { i ->
                            val e = events[i]
                            Card(shape = RoundedCornerShape(16.dp)) {
                                Column(Modifier.padding(14.dp)) {
                                    Row(Modifier.fillMaxWidth()) {
                                        Column(Modifier.weight(1f)) {
                                            Text(e.title, fontWeight = FontWeight.Bold)
                                            Spacer(Modifier.height(6.dp))
                                            Text(e.date, color = Color(0xFF64748B), style = MaterialTheme.typography.labelMedium)
                                            Text(e.venue, color = Color(0xFF64748B), style = MaterialTheme.typography.labelMedium)
                                        }
                                        IconButton(onClick = { /* TODO: edit dialog */ }) { Icon(Icons.Outlined.Edit, null) }
                                        IconButton(onClick = { vm.delete(e.id.toString()) }) { Icon(Icons.Outlined.Delete, null, tint = Color(0xFFEF4444)) }
                                    }
                                    Spacer(Modifier.height(8.dp))
                                    AssistChip(onClick = {}, label = { Text(e.category.name) })
                                    Spacer(Modifier.height(8.dp))
                                    Text(e.description, color = Color(0xFF334155))
                                }
                            }
                        }
                    }
                }
            }

            FloatingScrollbar(listState = state, modifier = Modifier.align(androidx.compose.ui.Alignment.CenterEnd))

            if (showCreate) {
                CreateEventDialog(
                    onDismiss = { showCreate = false },
                    onCreate = { title, date, time, location, category, desc ->
                        vm.upsert(
                            com.campusconnectplus.data.repository.Event(
                                title = title,
                                date = if (time.isNotBlank()) "$date $time" else date,
                                venue = location,
                                description = desc,
                                category = when (category) {
                                    "Cultural" -> com.campusconnectplus.data.repository.EventCategory.CULTURAL
                                    "Sports" -> com.campusconnectplus.data.repository.EventCategory.SPORTS
                                    else -> com.campusconnectplus.data.repository.EventCategory.ACADEMIC
                                }
                            )
                        )
                        showCreate = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateEventDialog(
    onDismiss: () -> Unit,
    onCreate: (String, String, String, String, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Academic") }
    var desc by remember { mutableStateOf("") }

    // Date Picker State
    val dateState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }
    var dateText by remember { mutableStateOf("") }

    // Time Picker State
    val timeState = rememberTimePickerState()
    var showTimePicker by remember { mutableStateOf(false) }
    var timeText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create New Event") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(title, { title = it }, label = { Text("Event Title") }, modifier = Modifier.fillMaxWidth())

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Date Field (Triggers Picker)
                    OutlinedTextField(
                        value = dateText,
                        onValueChange = {},
                        label = { Text("Date") },
                        modifier = Modifier
                            .weight(1f)
                            .clickable { showDatePicker = true },
                        enabled = false,
                        trailingIcon = { Icon(Icons.Outlined.CalendarToday, null) },
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledBorderColor = MaterialTheme.colorScheme.outline,
                            disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )

                    // Time Field (Triggers Picker)
                    OutlinedTextField(
                        value = timeText,
                        onValueChange = {},
                        label = { Text("Time") },
                        modifier = Modifier
                            .weight(1f)
                            .clickable { showTimePicker = true },
                        enabled = false,
                        trailingIcon = { Icon(Icons.Outlined.Schedule, null) },
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledBorderColor = MaterialTheme.colorScheme.outline,
                            disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }

                OutlinedTextField(location, { location = it }, label = { Text("Location") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(category, { category = it }, label = { Text("Category") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(desc, { desc = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(
                onClick = { onCreate(title, dateText, timeText, location, category, desc) },
                colors = ButtonDefaults.buttonColors(containerColor = AdminColors.Primary)
            ) { Text("Create Event") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )

    // Date Picker Dialog
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    dateState.selectedDateMillis?.let {
                        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                        dateText = sdf.format(Date(it))
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showDatePicker = false }) { Text("Cancel") } }
        ) {
            DatePicker(state = dateState)
        }
    }

    // Time Picker Dialog
    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val cal = Calendar.getInstance()
                    cal.set(Calendar.HOUR_OF_DAY, timeState.hour)
                    cal.set(Calendar.MINUTE, timeState.minute)
                    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
                    timeText = sdf.format(cal.time)
                    showTimePicker = false
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showTimePicker = false }) { Text("Cancel") } },
            text = {
                TimePicker(state = timeState)
            }
        )
    }
}

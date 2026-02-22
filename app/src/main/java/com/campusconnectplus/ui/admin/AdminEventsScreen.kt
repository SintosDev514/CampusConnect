package com.campusconnectplus.ui.admin

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.campusconnectplus.core.ui.components.FloatingScrollbar
import com.campusconnectplus.feature_admin.events.AdminEventsViewModel

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
                        vm.upsert(com.campusconnectplus.data.repository.Event(title = title, date = if (time.isNotBlank()) "$date $time" else date, venue = location, description = desc, category = when (category) { "Cultural" -> com.campusconnectplus.data.repository.EventCategory.CULTURAL; "Sports" -> com.campusconnectplus.data.repository.EventCategory.SPORTS; else -> com.campusconnectplus.data.repository.EventCategory.ACADEMIC }))
                        showCreate = false
                    }
                )
            }
        }
    }
}

@Composable
private fun CreateEventDialog(
    onDismiss: () -> Unit,
    onCreate: (String, String, String, String, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Academic") }
    var desc by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create New Event") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(title, { title = it }, label = { Text("Event Title") }, modifier = Modifier.fillMaxWidth())
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(date, { date = it }, label = { Text("Date") }, modifier = Modifier.weight(1f))
                    OutlinedTextField(time, { time = it }, label = { Text("Time") }, modifier = Modifier.weight(1f))
                }
                OutlinedTextField(location, { location = it }, label = { Text("Location") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(category, { category = it }, label = { Text("Category") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(desc, { desc = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(
                onClick = { onCreate(title, date, time, location, category, desc) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0B2A6B))
            ) { Text("Create Event") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

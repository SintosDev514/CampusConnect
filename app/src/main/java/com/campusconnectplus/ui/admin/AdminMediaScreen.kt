package com.campusconnectplus.ui.admin

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.campusconnectplus.core.ui.components.FloatingScrollbar
import com.campusconnectplus.feature_admin.media.AdminMediaViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AdminMediaScreen(vm: AdminMediaViewModel) {
    val state = rememberLazyListState()
    val media by vm.media.collectAsState()
    var showUpload by remember { mutableStateOf(false) }

    CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
        Box(Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxSize()) {
                TopBar(title = "Manage Media", subtitle = "${media.size} items • ${vm.totalSizeMb}MB total") {
                    showUpload = true
                }

                Spacer(Modifier.height(10.dp))

                if (media.isEmpty()) {
                    EmptyAdminPanel(
                        iconText = "⬆️",
                        title = "No media uploaded yet",
                        hint = "Click “Upload” to add photos and videos."
                    )
                } else {
                    LazyColumn(
                        state = state,
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 84.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(media.size) { i ->
                            val m = media[i]
                            Card(shape = RoundedCornerShape(16.dp)) {
                                Row(Modifier.padding(14.dp)) {
                                    Box(
                                        Modifier
                                            .size(56.dp)
                                            .background(Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                                    )
                                    Spacer(Modifier.width(12.dp))
                                    Column(Modifier.weight(1f)) {
                                        Text(m.fileName, fontWeight = FontWeight.Bold)
                                        Spacer(Modifier.height(4.dp))
                                        Text("${m.sizeMb}MB • ${m.date}", color = Color(0xFF64748B), style = MaterialTheme.typography.labelMedium)
                                    }
                                    IconButton(onClick = { vm.delete(m.id.toString()) }) {
                                        Icon(Icons.Outlined.Delete, null, tint = Color(0xFFEF4444))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            FloatingScrollbar(listState = state, modifier = Modifier.align(androidx.compose.ui.Alignment.CenterEnd))

            if (showUpload) {
                UploadMediaDialog(
                    onDismiss = { showUpload = false },
                    onUpload = { fileName, type, tags, offline ->
                        vm.upsert(com.campusconnectplus.data.repository.Media(eventId = 0L, url = "", type = if (type == "Video") com.campusconnectplus.data.repository.MediaType.VIDEO else com.campusconnectplus.data.repository.MediaType.IMAGE, title = fileName, fileName = fileName, date = "", sizeMb = 0))
                        showUpload = false
                    }
                )
            }
        }
    }
}

@Composable
private fun UploadMediaDialog(
    onDismiss: () -> Unit,
    onUpload: (String, String, String, Boolean) -> Unit
) {
    var fileName by remember { mutableStateOf("") }
    var mediaType by remember { mutableStateOf("Photo") }
    var tags by remember { mutableStateOf("") }
    var offline by remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Upload Media") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(fileName, { fileName = it }, label = { Text("File Name") }, modifier = Modifier.fillMaxWidth())

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    FilterChip(
                        selected = mediaType == "Photo",
                        onClick = { mediaType = "Photo" },
                        label = { Text("Photo") }
                    )
                    FilterChip(
                        selected = mediaType == "Video",
                        onClick = { mediaType = "Video" },
                        label = { Text("Video") }
                    )
                }

                OutlinedTextField(tags, { tags = it }, label = { Text("Tags (comma separated)") }, modifier = Modifier.fillMaxWidth())

                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Text("Offline Availability", modifier = Modifier.weight(1f))
                    Switch(checked = offline, onCheckedChange = { offline = it })
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onUpload(fileName, mediaType, tags, offline) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0B2A6B))
            ) {
                Icon(Icons.Outlined.Upload, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Upload Media")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

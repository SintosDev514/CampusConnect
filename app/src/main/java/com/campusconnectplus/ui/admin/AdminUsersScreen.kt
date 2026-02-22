package com.campusconnectplus.ui.admin

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.campusconnectplus.core.ui.components.FloatingScrollbar
import com.campusconnectplus.data.repository.UserRole
import com.campusconnectplus.feature_admin.users.AdminUsersViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AdminUsersScreen(vm: AdminUsersViewModel) {
    val state = rememberLazyListState()
    val users by vm.users.collectAsState()
    var query by remember { mutableStateOf("") }

    CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
        Box(Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxSize()) {
                TopBar(title = "User & Role Management", subtitle = "${users.size} total users")

                Spacer(Modifier.height(10.dp))

                // summary cards row
                Row(
                    Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatBox("Total", vm.totalCount().toString(), Modifier.weight(1f))
                    StatBox("Active", vm.activeCount().toString(), Modifier.weight(1f))
                    StatBox("Admins", vm.adminCount().toString(), Modifier.weight(1f))
                    StatBox("Students", vm.studentCount().toString(), Modifier.weight(1f))
                }

                Spacer(Modifier.height(10.dp))

                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it; vm.setSearch(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    placeholder = { Text("Search users...") },
                    leadingIcon = { Icon(Icons.Outlined.Search, null) },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(Modifier.height(10.dp))

                LazyColumn(
                    state = state,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 84.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(users.size) { i ->
                        val u = users[i]
                        UserCard(
                            name = u.name,
                            email = u.email,
                            role = roleDisplay(u.role),
                            active = u.active,
                            onRoleChange = { vm.setRole(u.id, it) },
                            onToggleActive = { vm.toggleActive(u.id) }
                        )
                    }
                }
            }

            FloatingScrollbar(listState = state, modifier = Modifier.align(androidx.compose.ui.Alignment.CenterEnd))
        }
    }
}

private fun roleDisplay(r: UserRole): String = when (r) {
    UserRole.ADMIN -> "Admin"
    UserRole.MEDIA_TEAM -> "Media Team"
    UserRole.ORGANIZER -> "Student Org"
    UserRole.STUDENT -> "Student"
}

@Composable
private fun StatBox(label: String, value: String, modifier: Modifier = Modifier) {
    Card(shape = RoundedCornerShape(14.dp), modifier = modifier) {
        Column(Modifier.padding(12.dp)) {
            Text(value, fontWeight = FontWeight.Bold)
            Text(label, color = Color(0xFF64748B), style = MaterialTheme.typography.labelSmall)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserCard(
    name: String,
    email: String,
    role: String,
    active: Boolean,
    onRoleChange: (String) -> Unit,
    onToggleActive: () -> Unit
) {
    Card(shape = RoundedCornerShape(16.dp)) {
        Column(Modifier.padding(14.dp)) {
            Text(name, fontWeight = FontWeight.Bold)
            Text(email, color = Color(0xFF64748B), style = MaterialTheme.typography.labelMedium)

            Spacer(Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AssistChip(onClick = {}, label = { Text(role) })
                AssistChip(
                    onClick = {},
                    label = { Text(if (active) "active" else "inactive") },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (active) Color(0xFF22C55E).copy(alpha = 0.15f) else Color(0xFFE2E8F0)
                    )
                )
            }

            Spacer(Modifier.height(10.dp))

            // role dropdown (matches your UI feel)
            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }, modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = role,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
                    label = { Text("Role") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) }
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    listOf("Student Org", "Faculty Staff", "Media Team", "Admin").forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = { onRoleChange(it); expanded = false }
                        )
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            OutlinedButton(onClick = onToggleActive, modifier = Modifier.align(androidx.compose.ui.Alignment.End)) {
                Text(if (active) "Deactivate" else "Activate")
            }
        }
    }
}

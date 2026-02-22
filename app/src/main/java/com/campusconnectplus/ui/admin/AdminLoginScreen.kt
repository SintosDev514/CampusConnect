package com.campusconnectplus.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AdminLoginScreen(
    onSignIn: (AdminRole, String, String) -> Unit
) {
    var role by remember { mutableStateOf(AdminRole.SystemAdmin) }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Surface(Modifier.fillMaxSize(), color = Color(0xFFF5F7FB)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(22.dp))

            Box(
                Modifier
                    .size(56.dp)
                    .background(Color(0xFF0B2A6B), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) { Icon(Icons.Outlined.Security, contentDescription = null, tint = Color.White) }

            Spacer(Modifier.height(14.dp))
            Text("CampusConnect+", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
            Text("Administrative Access Portal", color = Color(0xFF64748B))

            Spacer(Modifier.height(22.dp))

            Card(
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Access Level", fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        RoleChip("System\nAdmin", role == AdminRole.SystemAdmin, Modifier.weight(1f)) { role = AdminRole.SystemAdmin }
                        RoleChip("Event\nOrganizer", role == AdminRole.EventOrganizer, Modifier.weight(1f)) { role = AdminRole.EventOrganizer }
                        RoleChip("Media\nTeam", role == AdminRole.MediaTeam, Modifier.weight(1f)) { role = AdminRole.MediaTeam }
                    }

                    Spacer(Modifier.height(14.dp))

                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username") },
                        placeholder = { Text("Enter your username") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        placeholder = { Text("Enter your password") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = { onSignIn(role, username, password) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0B2A6B))
                    ) { Text("Sign In") }

                    Spacer(Modifier.height(10.dp))
                    Text(
                        "Secure local authentication · Version 1.0.2",
                        color = Color(0xFF64748B),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Spacer(Modifier.height(18.dp))
            Text(
                "This system is for authorized personnel only.\nAll access is logged and monitored.",
                color = Color(0xFF94A3B8),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
private fun RoleChip(text: String, selected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val border = if (selected) Color(0xFF0B2A6B) else Color(0xFFE2E8F0)
    val bg = if (selected) Color(0xFF0B2A6B).copy(alpha = 0.08f) else Color.White
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, border),
        color = bg,
        modifier = modifier
    ) {
        Box(Modifier.height(56.dp), contentAlignment = Alignment.Center) {
            Text(text, style = MaterialTheme.typography.labelMedium)
        }
    }
}

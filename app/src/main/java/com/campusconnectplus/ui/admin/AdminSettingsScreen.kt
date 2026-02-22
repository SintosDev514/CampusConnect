package com.campusconnectplus.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AdminSettingsScreen(onLogout: () -> Unit = {}) {
    var autoSync by remember { mutableStateOf(true) }
    var offlineMode by remember { mutableStateOf(false) }
    var notifications by remember { mutableStateOf(true) }
    var compactView by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize().background(AdminColors.Background)) {
        TopBar(title = "System Settings", subtitle = "Configure app preferences and data")

        Column(
            Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, AdminColors.Border)
            ) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("App Preferences", fontWeight = FontWeight.Bold, color = AdminColors.Dark)
                    ToggleRow("Auto Sync", "Automatically sync data when online", autoSync) { autoSync = it }
                    ToggleRow("Offline Mode", "Enable full offline functionality", offlineMode) { offlineMode = it }
                    ToggleRow("Notifications", "Show system notifications", notifications) { notifications = it }
                    ToggleRow("Compact View", "Use condensed layout for lists", compactView) { compactView = it }
                }
            }

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, AdminColors.Border)
            ) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Data Management", fontWeight = FontWeight.Bold, color = AdminColors.Dark)
                    OutlinedButton(
                        onClick = {}, 
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("Export Data Backup") }
                    
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("Reset All Data", fontWeight = FontWeight.Bold) }
                }
            }

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, AdminColors.Border)
            ) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("System Information", fontWeight = FontWeight.Bold, color = AdminColors.Dark)
                    InfoRow("App Version", "1.0.2")
                    InfoRow("Database", "Room (Local)")
                    InfoRow("Status", "Operational", valueColor = Color(0xFF10B981))
                }
            }

            Spacer(Modifier.height(12.dp))

            // Professional Logout Button
            Button(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFFEF4444)
                ),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFEE2E2)),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Icon(Icons.Outlined.Logout, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
                Text("Sign Out", fontWeight = FontWeight.ExtraBold)
            }
            
            Spacer(Modifier.height(80.dp)) // Padding for bottom bar
        }
    }
}

@Composable
private fun ToggleRow(title: String, subtitle: String, checked: Boolean, onChecked: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column(Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.SemiBold, color = AdminColors.Dark)
            Text(subtitle, color = AdminColors.Secondary, style = MaterialTheme.typography.bodySmall)
        }
        Switch(
            checked = checked, 
            onCheckedChange = onChecked,
            colors = SwitchDefaults.colors(checkedTrackColor = AdminColors.Primary)
        )
    }
}

@Composable
private fun InfoRow(label: String, value: String, valueColor: Color = AdminColors.Dark) {
    Row(Modifier.fillMaxWidth()) {
        Text(label, modifier = Modifier.weight(1f), color = AdminColors.Secondary)
        Text(value, color = valueColor, fontWeight = FontWeight.Bold)
    }
}

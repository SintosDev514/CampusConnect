package com.campusconnectplus.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun AdminLoginScreen(
    onSignIn: (AdminRole, String, String) -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    var role by remember { mutableStateOf(AdminRole.SystemAdmin) }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    
    var usernameError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var passwordVisible by remember { mutableStateOf(false) }

    fun validate(): Boolean {
        var isValid = true
        if (username.isBlank()) {
            usernameError = "Username is required"
            isValid = false
        } else if (username.length < 3) {
            usernameError = "Username too short"
            isValid = false
        } else {
            usernameError = null
        }

        if (password.isBlank()) {
            passwordError = "Password is required"
            isValid = false
        } else if (password.length < 6) {
            passwordError = "Password must be at least 6 characters"
            isValid = false
        } else {
            passwordError = null
        }
        return isValid
    }

    Surface(Modifier.fillMaxSize(), color = AdminColors.Background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(40.dp))

            Box(
                Modifier
                    .size(64.dp)
                    .background(AdminColors.Primary, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) { Icon(Icons.Outlined.Lock, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp)) }

            Spacer(Modifier.height(16.dp))
            Text("CampusConnect+", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineSmall, color = AdminColors.Dark)
            Text("Administrative Access Portal", color = AdminColors.Secondary)

            Spacer(Modifier.height(32.dp))

            Card(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, AdminColors.Border)
            ) {
                Column(Modifier.padding(24.dp)) {
                    Text("Select Access Level", fontWeight = FontWeight.Bold, color = AdminColors.Dark)
                    Spacer(Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        RoleChip("Admin", role == AdminRole.SystemAdmin, Modifier.weight(1f)) { role = AdminRole.SystemAdmin }
                        RoleChip("Staff", role == AdminRole.EventOrganizer, Modifier.weight(1f)) { role = AdminRole.EventOrganizer }
                        RoleChip("Media", role == AdminRole.MediaTeam, Modifier.weight(1f)) { role = AdminRole.MediaTeam }
                    }

                    Spacer(Modifier.height(24.dp))

                    OutlinedTextField(
                        value = username,
                        onValueChange = { 
                            username = it
                            if (usernameError != null) usernameError = null
                        },
                        label = { Text("Username or Email") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = usernameError != null,
                        supportingText = { usernameError?.let { Text(it) } },
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { 
                            password = it
                            if (passwordError != null) passwordError = null
                        },
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            val image = if (passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(image, null)
                            }
                        },
                        isError = passwordError != null,
                        supportingText = { passwordError?.let { Text(it) } },
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(Modifier.height(24.dp))

                    Button(
                        onClick = { if (validate()) onSignIn(role, username, password) },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AdminColors.Primary)
                    ) { Text("Sign In", fontWeight = FontWeight.Bold) }

                    Spacer(Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Don't have an account?", style = MaterialTheme.typography.bodySmall, color = AdminColors.Secondary)
                        TextButton(onClick = onNavigateToSignUp) {
                            Text("Sign Up", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = AdminColors.Primary)
                        }
                    }
                }
            }

            Spacer(Modifier.weight(1f))
            Text(
                "Authorized Access Only · v1.0.2",
                color = AdminColors.Secondary,
                style = MaterialTheme.typography.labelSmall
            )
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun AdminSignUpScreen(
    onSignUp: (AdminRole, String, String, String) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var role by remember { mutableStateOf(AdminRole.EventOrganizer) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    
    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    fun validate(): Boolean {
        var isValid = true
        if (name.isBlank()) { nameError = "Name is required"; isValid = false } else nameError = null
        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) { 
            emailError = "Valid email is required"; isValid = false 
        } else emailError = null
        if (password.length < 6) { passwordError = "Min 6 characters required"; isValid = false } else passwordError = null
        return isValid
    }

    Surface(Modifier.fillMaxSize(), color = AdminColors.Background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(40.dp))
            Text("Create Admin Account", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineSmall, color = AdminColors.Dark)
            Text("Register for the administrative portal", color = AdminColors.Secondary)
            Spacer(Modifier.height(32.dp))

            Card(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, AdminColors.Border)
            ) {
                Column(Modifier.padding(24.dp)) {
                    Text("Select Role", fontWeight = FontWeight.Bold, color = AdminColors.Dark)
                    Spacer(Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        RoleChip("Staff", role == AdminRole.EventOrganizer, Modifier.weight(1f)) { role = AdminRole.EventOrganizer }
                        RoleChip("Media", role == AdminRole.MediaTeam, Modifier.weight(1f)) { role = AdminRole.MediaTeam }
                    }

                    Spacer(Modifier.height(24.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it; nameError = null },
                        label = { Text("Full Name") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = nameError != null,
                        supportingText = { nameError?.let { Text(it) } },
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it; emailError = null },
                        label = { Text("Email Address") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = emailError != null,
                        supportingText = { emailError?.let { Text(it) } },
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it; passwordError = null },
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(),
                        isError = passwordError != null,
                        supportingText = { passwordError?.let { Text(it) } },
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(Modifier.height(24.dp))
                    Button(
                        onClick = { if (validate()) onSignUp(role, name, email, password) },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AdminColors.Primary)
                    ) { Text("Create Account", fontWeight = FontWeight.Bold) }
                    Spacer(Modifier.height(16.dp))
                    TextButton(onClick = onNavigateToLogin, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        Text("Already have an account? Sign In", style = MaterialTheme.typography.bodySmall, color = AdminColors.Primary)
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun RoleChip(text: String, selected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val border = if (selected) AdminColors.Primary else AdminColors.Border
    val bg = if (selected) AdminColors.Primary.copy(alpha = 0.08f) else Color.White
    val contentColor = if (selected) AdminColors.Primary else AdminColors.Secondary
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, border),
        color = bg,
        modifier = modifier
    ) {
        Box(Modifier.height(56.dp), contentAlignment = Alignment.Center) {
            Text(text, style = MaterialTheme.typography.labelMedium, fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium, color = contentColor)
        }
    }
}

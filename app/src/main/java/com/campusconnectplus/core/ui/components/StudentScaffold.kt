package com.campusconnectplus.core.ui.components

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.campusconnectplus.ui.student.StudentBottomBar

@Composable
fun StudentScaffold(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    content: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = { StudentBottomBar(currentRoute = currentRoute, onNavigate = onNavigate) }
    ) { _ ->
        content()
    }
}

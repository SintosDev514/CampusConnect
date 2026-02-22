package com.campusconnectplus.core.ui.components

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.campusconnectplus.ui.admin.AdminBottomBar

@Composable
fun AdminScaffold(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    content: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = { AdminBottomBar(currentRoute = currentRoute, onNavigate = onNavigate) }
    ) { _ ->
        content()
    }
}

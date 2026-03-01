package com.campusconnectplus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.campusconnectplus.core.di.ServiceLocator
import com.campusconnectplus.core.di.ServiceLocatorSeed
import com.campusconnectplus.core.ui.components.AdminScaffold
import com.campusconnectplus.core.ui.components.StudentScaffold
import com.campusconnectplus.feature_admin.AdminDashboardViewModel
import com.campusconnectplus.feature_admin.announcements.AdminAnnouncementsViewModel
import com.campusconnectplus.feature_admin.events.AdminEventsViewModel
import com.campusconnectplus.feature_admin.media.AdminMediaViewModel
import com.campusconnectplus.feature_admin.users.AdminUsersViewModel
import com.campusconnectplus.feature_student.events.StudentEventsViewModel
import com.campusconnectplus.feature_student.media.StudentMediaViewModel
import com.campusconnectplus.feature_student.saved.StudentSavedViewModel
import com.campusconnectplus.ui.admin.*
import com.campusconnectplus.ui.student.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val container = ServiceLocator.provideContainer(this)

        setContent {
            Surface(color = MaterialTheme.colorScheme.background) {
                val nav = rememberNavController()

                // Seed demo data ONCE (so Room has real data)
                LaunchedEffect(Unit) {
                    ServiceLocatorSeed.ensureSeed(this@MainActivity, container)
                }

                // ViewModels (constructor injection)
                val studentEventsVm = remember {
                    StudentEventsViewModel(container.eventRepository, container.favoriteRepository)
                }
                val studentMediaVm = remember {
                    StudentMediaViewModel(container.mediaRepository, container.favoriteRepository)
                }

                val adminEventsVm = remember { AdminEventsViewModel(container.eventRepository) }
                val adminMediaVm = remember { AdminMediaViewModel(container.mediaRepository) }
                val adminAnnVm = remember { AdminAnnouncementsViewModel(container.announcementRepository) }
                val adminUsersVm = remember { AdminUsersViewModel(container.userRepository) }
                val adminDashboardVm = remember {
                    AdminDashboardViewModel(
                        container.eventRepository,
                        container.mediaRepository,
                        container.announcementRepository,
                        container.userRepository
                    )
                }
                val studentSavedVm = remember {
                    StudentSavedViewModel(
                        container.eventRepository,
                        container.mediaRepository,
                        container.favoriteRepository
                    )
                }

                NavHost(navController = nav, startDestination = StudentTab.Home.route) {

                    // ---------- STUDENT ----------
                    composable(StudentTab.Home.route) {
                        StudentScaffold(
                            currentRoute = StudentTab.Home.route,
                            onNavigate = nav::navigate
                        ) {
                            StudentHomeScreen(
                                onQuickNavigateEvents = { nav.navigate(StudentTab.Events.route) },
                                onQuickNavigateMedia = { nav.navigate(StudentTab.Media.route) },
                                onQuickNavigateSaved = { nav.navigate(StudentTab.Saved.route) },
                                onQuickNavigateAnnouncements = { nav.navigate(AdminTab.Announcements.route) },
                                onNavigateToAdmin = { nav.navigate("admin/login") }
                            )
                        }
                    }

                    composable(StudentTab.Events.route) {
                        StudentScaffold(
                            currentRoute = StudentTab.Events.route,
                            onNavigate = nav::navigate
                        ) {
                            StudentEventsScreen(vm = studentEventsVm)
                        }
                    }

                    composable(StudentTab.Media.route) {
                        StudentScaffold(
                            currentRoute = StudentTab.Media.route,
                            onNavigate = nav::navigate
                        ) {
                            StudentMediaScreen(vm = studentMediaVm)
                        }
                    }

                    composable(StudentTab.Saved.route) {
                        StudentScaffold(currentRoute = StudentTab.Saved.route, onNavigate = nav::navigate) {
                            StudentSavedScreen(vm = studentSavedVm)
                        }
                    }

                    // ---------- ADMIN LOGIN / SIGNUP ----------
                    composable("admin/login") {
                        AdminLoginScreen(
                            onSignIn = { _, _, _ ->
                                nav.navigate(AdminTab.Dashboard.route) {
                                    popUpTo("admin/login") { inclusive = true }
                                }
                            },
                            onNavigateToSignUp = { nav.navigate("admin/signup") }
                        )
                    }

                    composable("admin/signup") {
                        AdminSignUpScreen(
                            onSignUp = { role, name, email, pass ->
                                // In a real app, save user to DB here using the role.
                                // For now, navigate back to login.
                                nav.navigate("admin/login") {
                                    popUpTo("admin/signup") { inclusive = true }
                                }
                            },
                            onNavigateToLogin = { nav.navigate("admin/login") }
                        )
                    }

                    // ---------- ADMIN ----------
                    composable(AdminTab.Dashboard.route) {
                        AdminScaffold(
                            currentRoute = AdminTab.Dashboard.route,
                            onNavigate = nav::navigate
                        ) {
                            AdminDashboardScreen(vm = adminDashboardVm)
                        }
                    }

                    composable(AdminTab.Events.route) {
                        AdminScaffold(
                            currentRoute = AdminTab.Events.route,
                            onNavigate = nav::navigate
                        ) {
                            AdminEventsScreen(vm = adminEventsVm)
                        }
                    }

                    composable(AdminTab.Media.route) {
                        AdminScaffold(
                            currentRoute = AdminTab.Media.route,
                            onNavigate = nav::navigate
                        ) {
                            AdminMediaScreen(vm = adminMediaVm)
                        }
                    }

                    composable(AdminTab.Announcements.route) {
                        AdminScaffold(
                            currentRoute = AdminTab.Announcements.route,
                            onNavigate = nav::navigate
                        ) {
                            AdminAnnouncementsScreen(vm = adminAnnVm)
                        }
                    }

                    composable(AdminTab.Users.route) {
                        AdminScaffold(
                            currentRoute = AdminTab.Users.route,
                            onNavigate = nav::navigate
                        ) {
                            AdminUsersScreen(vm = adminUsersVm)
                        }
                    }

                    composable(AdminTab.Settings.route) {
                        AdminScaffold(
                            currentRoute = AdminTab.Settings.route,
                            onNavigate = nav::navigate
                        ) {
                            AdminSettingsScreen(onLogout = {
                                nav.navigate(StudentTab.Home.route) {
                                    popUpTo(StudentTab.Home.route) { inclusive = true }
                                }
                            })
                        }
                    }
                }
            }
        }
    }
}

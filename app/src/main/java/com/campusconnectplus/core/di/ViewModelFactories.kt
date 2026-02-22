package com.campusconnectplus.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.campusconnectplus.feature_admin.announcements.AdminAnnouncementsViewModel
import com.campusconnectplus.feature_admin.events.AdminEventsViewModel
import com.campusconnectplus.feature_admin.media.AdminMediaViewModel
import com.campusconnectplus.feature_admin.users.AdminUsersViewModel
import com.campusconnectplus.feature_student.events.StudentEventsViewModel
import com.campusconnectplus.feature_student.media.StudentMediaViewModel

object ViewModelFactories {

    fun studentEventsFactory(container: AppContainer): ViewModelProvider.Factory = factory {
        StudentEventsViewModel(container.eventRepository, container.favoriteRepository)
    }

    fun studentMediaFactory(container: AppContainer): ViewModelProvider.Factory = factory {
        StudentMediaViewModel(container.mediaRepository, container.favoriteRepository)
    }

    fun adminEventsFactory(container: AppContainer): ViewModelProvider.Factory = factory {
        AdminEventsViewModel(container.eventRepository)
    }

    fun adminMediaFactory(container: AppContainer): ViewModelProvider.Factory = factory {
        AdminMediaViewModel(container.mediaRepository)
    }

    fun adminAnnouncementsFactory(container: AppContainer): ViewModelProvider.Factory = factory {
        AdminAnnouncementsViewModel(container.announcementRepository)
    }

    fun adminUsersFactory(container: AppContainer): ViewModelProvider.Factory = factory {
        AdminUsersViewModel(container.userRepository)
    }

    private inline fun <reified VM : ViewModel> factory(
        crossinline create: () -> VM
    ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = create() as T
    }
}


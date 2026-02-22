package com.campusconnectplus.feature_student.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campusconnectplus.data.repository.EventRepository
import com.campusconnectplus.data.repository.FavoriteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StudentEventsViewModel(
    private val eventRepo: EventRepository,
    private val favoriteRepo: FavoriteRepository
) : ViewModel() {

    val events = eventRepo.observeEvents()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val favoriteEventIds: StateFlow<Set<Long>> =
        favoriteRepo.observeFavoriteEventIds()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet())

    fun toggleFavorite(eventId: Long) {
        viewModelScope.launch { favoriteRepo.toggleEvent(eventId) }
    }
}

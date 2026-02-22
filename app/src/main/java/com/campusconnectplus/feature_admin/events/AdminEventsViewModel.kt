package com.campusconnectplus.feature_admin.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campusconnectplus.data.repository.Event
import com.campusconnectplus.data.repository.EventRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AdminEventsViewModel(
    private val repo: EventRepository
) : ViewModel() {

    val events: StateFlow<List<Event>> =
        repo.observeEvents().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun delete(eventId: String) {
        viewModelScope.launch { repo.delete(eventId) }
    }

    fun upsert(event: Event) {
        viewModelScope.launch { repo.upsert(event) }
    }
}

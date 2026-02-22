package com.campusconnectplus.feature_admin.media

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import androidx.lifecycle.viewModelScope
import com.campusconnectplus.data.repository.Media
import com.campusconnectplus.data.repository.MediaRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class AdminMediaViewModel(
    private val repo: MediaRepository
) : ViewModel() {

    private val eventId = MutableStateFlow<String?>(null)

    val media: StateFlow<List<Media>> =
        eventId.flatMapLatest { id ->
            if (id == null) repo.observeMedia()
            else repo.ofEvent(id)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val totalSizeMb: Float get() = media.value.sumOf { it.sizeMb }.toFloat()

    fun setEvent(eventId: String) { this.eventId.value = eventId }

    fun upsert(media: Media) {
        viewModelScope.launch { repo.upsert(media) }
    }

    fun delete(id: String) {
        viewModelScope.launch { repo.delete(id) }
    }
}

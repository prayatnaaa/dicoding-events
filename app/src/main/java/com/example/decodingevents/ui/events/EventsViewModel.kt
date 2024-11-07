package com.example.decodingevents.ui.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.decodingevents.data.local.entity.Event
import com.example.decodingevents.data.source.EventRepository
import kotlinx.coroutines.launch

class EventsViewModel(private val eventRepository: EventRepository) : ViewModel() {

    fun getListEvent(active: String, isActive: Boolean) = eventRepository.getEvents(active, isActive)
    fun getActiveEvents() = eventRepository.getActiveEvents()
    fun getFinishedEvent() = eventRepository.getFinishedEvents()
    fun getEventById(id: String, isActive: Boolean) = eventRepository.getEventById(id, isActive )
    fun getFavouriteEvent() = eventRepository.getFavouriteEvent()

    fun saveEvent(event: Event) {
        viewModelScope.launch {
            eventRepository.setFavouriteEvent(event, true)
        }
    }

    fun deleteNews(event: Event) {
        viewModelScope.launch {
            eventRepository.setFavouriteEvent(event, false)
        }
    }
}
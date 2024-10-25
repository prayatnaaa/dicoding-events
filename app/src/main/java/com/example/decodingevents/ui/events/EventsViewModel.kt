package com.example.decodingevents.ui.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.decodingevents.data.local.entity.Event
import com.example.decodingevents.data.repository.EventRepository
import kotlinx.coroutines.launch

class EventsViewModel(private val eventRepository: EventRepository) : ViewModel() {

    fun getListEvent(active: String) = eventRepository.getEvents(active)
    fun getEventById(id: String) = eventRepository.getEventById(id)
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
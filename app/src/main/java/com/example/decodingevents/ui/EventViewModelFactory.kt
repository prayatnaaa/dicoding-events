package com.example.decodingevents.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.decodingevents.data.di.Injection
import com.example.decodingevents.data.repository.EventRepository
import com.example.decodingevents.ui.events.EventsViewModel

class EventViewModelFactory private constructor(private val mEventRepository: EventRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventsViewModel::class.java)) {
            return EventsViewModel(mEventRepository) as T
        }
        throw IllegalArgumentException("Unknown View Model Class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: EventViewModelFactory? = null
        fun getInstance(context: Context): EventViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: EventViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}
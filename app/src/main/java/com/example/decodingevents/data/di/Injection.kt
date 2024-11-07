package com.example.decodingevents.data.di

import android.content.Context
import com.example.decodingevents.data.local.room.EventDatabase
import com.example.decodingevents.data.remote.retrofit.ApiConfig
import com.example.decodingevents.data.source.EventRepository
import com.example.decodingevents.util.AppExecutors

object Injection {
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val database = EventDatabase.getInstance(context)
        val dao = database.eventDao()
        val appExecutors = AppExecutors()

        return EventRepository.getInstance(apiService, dao, appExecutors)
    }
}
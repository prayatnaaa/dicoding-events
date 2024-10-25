package com.example.decodingevents.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.decodingevents.data.Result
import com.example.decodingevents.data.local.entity.Event
import com.example.decodingevents.data.local.room.EventDao
import com.example.decodingevents.data.remote.retrofit.ApiService
import com.example.decodingevents.util.AppExecutors

class EventRepository private constructor(
    private val apiService: ApiService,
    private val eventDao: EventDao,
    private val appExecutors: AppExecutors
) {

    fun getEventById(id: String): LiveData<Result<Event>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getDetailEventById(id)
            val event = response.event
            val isFavourite = eventDao.isEventFavourite(event.name)
            Event(
                event.id,
                event.summary,
                event.mediaCover,
                event.registrants,
                event.imageLogo,
                event.link,
                event.description,
                event.ownerName,
                event.cityName,
                event.quota,
                event.name,
                event.beginTime,
                event.endTime,
                event.category,
                isFavourite
            )
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<Event>> =
            eventDao.getEventById(id).map { Result.Success(it) }
        emitSource(localData)
    }

    fun getFavouriteEvent(): LiveData<List<Event>> {
        return eventDao.getFavouriteEvent()
    }

    suspend fun setFavouriteEvent(event: Event, isFavourite: Boolean) {
        event.isFavourite = isFavourite
        eventDao.updateEvent(event)
    }

    fun getEvents(active: String): LiveData<Result<List<Event>>> = liveData {
        emit(Result.Loading)
        try {
            val events = when (active) {
                "1" -> {
                    val response = apiService.getListEvents("1")
                    response.listEvents
                }
                "0" -> {
                    val response = apiService.getListEvents("0")
                    response.listEvents
                }
                else -> {
                    emptyList()
                }
            }
            Log.d("EventRepository", "active: $active, size: ${events.count()}")
            val listEvent = events.map { event ->
                val isFav = eventDao.isEventFavourite(event.name)
                Event(
                    event.id,
                    event.summary,
                    event.mediaCover,
                    event.registrants,
                    event.imageLogo,
                    event.link,
                    event.description,
                    event.ownerName,
                    event.cityName,
                    event.quota,
                    event.name,
                    event.beginTime,
                    event.endTime,
                    event.category,
                    isFav
                )
            }
            eventDao.deleteAll()
            eventDao.insertEvent(listEvent)
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<Event>>> = eventDao.getEvents().map {
            Result.Success(it)
        }
        emitSource(localData)

    }


    companion object {
        @Volatile
        private var instance: EventRepository? = null
        fun getInstance(
            apiService: ApiService,
            eventDao: EventDao,
            appExecutors: AppExecutors
        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService, eventDao, appExecutors)
            }.also { instance = it }

    }
}
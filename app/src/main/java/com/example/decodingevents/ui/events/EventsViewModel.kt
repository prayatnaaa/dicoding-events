package com.example.decodingevents.ui.events

import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.decodingevents.data.remote.resource.DetailEventResponse
import com.example.decodingevents.data.remote.resource.EventResponse
import com.example.decodingevents.data.remote.resource.ListEventsItem
import com.example.decodingevents.data.remote.retrofit.ApiConfig
import com.example.decodingevents.ui.events.EventsAdapter
import com.example.decodingevents.util.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventsViewModel : ViewModel() {

    private var _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage

    private var _detailEvent = MutableLiveData<DetailEventResponse>()
    val detailEvent: LiveData<DetailEventResponse> = _detailEvent

    private var _upcomingEvents = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvents: LiveData<List<ListEventsItem>> = _upcomingEvents

    private var _finishedEvents = MutableLiveData<List<ListEventsItem>>()
    val finishedEvents: LiveData<List<ListEventsItem>> = _finishedEvents


    fun getEventById(id: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getDetailEventById(id)
        client.enqueue(object : Callback<DetailEventResponse> {
            override fun onResponse(
                call: Call<DetailEventResponse>, response: Response<DetailEventResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailEvent.value = response.body()
                } else {
                    _isError.value = true
                    _errorMessage.value = Event(response.message().toString())
                }
            }

            override fun onFailure(call: Call<DetailEventResponse>, t: Throwable) {
                _isError.value = true
                _errorMessage.value = Event("Something went wrong!")
            }

        })
    }

    fun searchEvent(query: String, adapter: EventsAdapter) {

        val client = ApiConfig.getApiService().searchEvent(keyword = query)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    _finishedEvents.value = response.body()?.listEvents
                    adapter.submitList(_finishedEvents.value)
                } else {
                    Log.e("EventsViewModel", "Failed to fetch search view")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                Log.e("EventsViewModel", "Failed to fetch search view")
            }

        })
    }

    fun listEvents(active: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getListEvents(active)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    when (active) {
                        "1" -> _upcomingEvents.value = response.body()?.listEvents
                        "0" -> _finishedEvents.value = response.body()?.listEvents
                    }
                } else {
                    _isLoading.value = false
                    _isError.value = true
                    _errorMessage.value = Event("Something when wrong!")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                _errorMessage.value = Event("Something went wrong!")
            }
        })
    }

    fun setLoading(loaded: Boolean, progressBar: ProgressBar) {
        if (!loaded) {
            progressBar.visibility = View.INVISIBLE
        }
    }
}
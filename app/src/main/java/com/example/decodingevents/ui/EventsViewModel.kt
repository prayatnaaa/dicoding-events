package com.example.decodingevents.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.decodingevents.data.resource.DetailEventResponse
import com.example.decodingevents.data.resource.EventResponse
import com.example.decodingevents.data.resource.ListEventsItem
import com.example.decodingevents.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventsViewModel : ViewModel() {

    private var _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

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
                call: Call<DetailEventResponse>,
                response: Response<DetailEventResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailEvent.value = response.body()
                } else {
                    _isError.value = true
                    _errorMessage.value = response.message()
                }
            }

            override fun onFailure(call: Call<DetailEventResponse>, t: Throwable) {
                _isError.value = true
                _errorMessage.value = t.message
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
                    _isError.value = true
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isError.value = true
                Log.e("EventsViewModel", t.message.toString())
            }

        })
    }


}
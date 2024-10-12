package com.example.decodingevents.data.retrofit

import com.example.decodingevents.data.resource.DetailEventResponse
import com.example.decodingevents.data.resource.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun getListEvents(@Query("active") active: String) : Call<EventResponse>

    @GET("events/{id}")
    fun getDetailEventById(@Path("id") id: String) : Call<DetailEventResponse>
}
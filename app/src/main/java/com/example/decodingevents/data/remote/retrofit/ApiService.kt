package com.example.decodingevents.data.remote.retrofit

import com.example.decodingevents.data.remote.resource.DetailEventResponse
import com.example.decodingevents.data.remote.resource.EventResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    suspend fun getListEvents(@Query("active") active: String): EventResponse

    @GET("events/{id}")
    suspend fun getDetailEventById(@Path("id") id: String): DetailEventResponse

    @GET("events")
    suspend fun searchEvent(
        @Query("active") active: String = "-1",
        @Query("q") keyword: String
    ) : EventResponse
}
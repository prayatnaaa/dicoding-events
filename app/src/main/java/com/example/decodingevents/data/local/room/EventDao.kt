package com.example.decodingevents.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.decodingevents.data.local.entity.Event

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEvent(event: List<Event>)

    @Query("SELECT * FROM event WHERE id = :id")
    fun getEventById(id: String): LiveData<Event>

    @Update
    suspend fun updateEvent(event: Event)

    @Query("SELECT * FROM event ORDER BY begin_time DESC")
    fun getEvents(): LiveData<List<Event>>

    @Query("DELETE FROM event WHERE isFavourite = 0")
    suspend fun deleteAll()

    @Query("SELECT * FROM event where isFavourite = 1")
    fun getFavouriteEvent(): LiveData<List<Event>>


    @Query("SELECT EXISTS(SELECT * FROM event WHERE name = :name AND isFavourite = 1)")
    suspend fun isEventFavourite(name: String): Boolean

}
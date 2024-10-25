package com.example.decodingevents.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.decodingevents.data.local.entity.Event

@Database(entities = [Event::class], version = 3, exportSchema = false)
abstract class EventDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao

    companion object {
        @Volatile
        private var instance: EventDatabase? = null
        fun getInstance(context: Context): EventDatabase =
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                EventDatabase::class.java, "Event.db"
            ).fallbackToDestructiveMigration().build()
    }
}
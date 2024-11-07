package com.example.decodingevents.data.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "themeSetting")

class ThemePreference private constructor(private val dataStore: DataStore<Preferences>){

    private val THEME_KEY = booleanPreferencesKey("theme_setting")
    private val REMINDER_KEY = booleanPreferencesKey("reminder_key")

    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[THEME_KEY] ?: false
        }
    }

    suspend fun getCurrentTheme(): Boolean {
        return dataStore.data.map { preferences ->
            preferences[THEME_KEY] ?: false
        }.first()
    }

    suspend fun setThemeSetting(isDarkModeActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDarkModeActive
        }
    }

    suspend fun setReminderActive(isActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[REMINDER_KEY] = isActive
        }
    }

     fun getReminderActive(): Flow<Boolean> {
        return dataStore.data.map {
            preferences -> preferences[REMINDER_KEY] ?: false
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ThemePreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): ThemePreference {
            return INSTANCE ?: synchronized(this) {
                val instance = ThemePreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
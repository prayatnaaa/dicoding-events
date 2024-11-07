package com.example.decodingevents.ui.settings

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.decodingevents.data.preference.ThemePreference
import com.example.decodingevents.helper.ReminderWork
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class ThemeViewModel(private val themePref: ThemePreference, application: Application) :
    ViewModel() {

    companion object {
        private const val UNIQUE_WORK = "Reminder"
    }

    private val workManager = WorkManager.getInstance(application)

    fun startReminder() {
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val workRequest = PeriodicWorkRequest.Builder(
            ReminderWork::class.java, 1, TimeUnit.DAYS
        ).setConstraints(constraints).build()

        workManager.enqueueUniquePeriodicWork(
            UNIQUE_WORK,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

    fun stopReminder() {
        workManager.cancelUniqueWork(UNIQUE_WORK)
    }

    fun setReminderSetting(isActive: Boolean) {
        viewModelScope.launch {
            themePref.setReminderActive(isActive)
        }
    }

     fun getReminderSettings(): LiveData<Boolean> {
        return themePref.getReminderActive().asLiveData()
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return themePref.getThemeSetting().asLiveData()
    }

    suspend fun getCurrentTheme() = themePref.getCurrentTheme()

    fun setThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            themePref.setThemeSetting(isDarkModeActive)
        }
    }
}
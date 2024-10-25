package com.example.decodingevents.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.decodingevents.data.preference.ThemePreference
import kotlinx.coroutines.launch

class SettingsViewModel(private val themePref: ThemePreference): ViewModel() {
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
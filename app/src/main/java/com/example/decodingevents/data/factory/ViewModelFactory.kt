package com.example.decodingevents.data.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.decodingevents.data.preference.ThemePreference
import com.example.decodingevents.ui.settings.ThemeViewModel

class ViewModelFactory(private val themePref: ThemePreference) : ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(ThemeViewModel::class.java)) {
            return ThemeViewModel(themePref) as T
        }

        throw IllegalArgumentException("Unknown class viewmodel: " + modelClass.name)
    }

}
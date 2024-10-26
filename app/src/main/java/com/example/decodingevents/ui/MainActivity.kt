package com.example.decodingevents.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.decodingevents.R
import com.example.decodingevents.data.factory.ViewModelFactory
import com.example.decodingevents.data.preference.ThemePreference
import com.example.decodingevents.data.preference.dataStore
import com.example.decodingevents.databinding.ActivityMainBinding
import com.example.decodingevents.ui.settings.ThemeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = ThemePreference.getInstance(this.application.dataStore)
        val themeViewModel = ViewModelProvider(
            this,
            ViewModelFactory(pref)
        )[ThemeViewModel::class.java]

        val isDarkModeActive = runBlocking {
            themeViewModel.getCurrentTheme()
        }

        checkDarkSetting(isDarkModeActive)
        themeViewModel.getThemeSettings().observe(this) {
            checkDarkSetting(it)
        }

        val navView: BottomNavigationView = binding.bottomNavView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        navView.setupWithNavController(navController)

    }

    private fun checkDarkSetting(isDark: Boolean) {
        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}
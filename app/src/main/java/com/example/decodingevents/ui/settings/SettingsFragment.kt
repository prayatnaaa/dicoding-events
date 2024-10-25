package com.example.decodingevents.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.decodingevents.data.factory.ViewModelFactory
import com.example.decodingevents.data.preference.ThemePreference
import com.example.decodingevents.data.preference.dataStore
import com.example.decodingevents.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = ThemePreference.getInstance(requireActivity().application.dataStore)
        val settingsViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(pref)
        )[SettingsViewModel::class.java]

        settingsViewModel.getThemeSettings().observe(viewLifecycleOwner) {
            checkDarkSetting(it)
        }

        binding.switchTheme.setOnCheckedChangeListener {
            _:CompoundButton, isChecked: Boolean ->
            settingsViewModel.setThemeSetting(isChecked)
        }

    }

    private fun checkDarkSetting(isDark: Boolean) {
        if (isDark) {
            binding.switchTheme.isChecked = true
        } else {
            binding.switchTheme.isChecked = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
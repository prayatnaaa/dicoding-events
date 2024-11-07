package com.example.decodingevents.ui.settings

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.decodingevents.R
import com.example.decodingevents.data.factory.ViewModelFactory
import com.example.decodingevents.data.preference.ThemePreference
import com.example.decodingevents.data.preference.dataStore
import com.example.decodingevents.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val registerPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.permission_success),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.permission_fail),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Build.VERSION.SDK_INT >= 33) {
            registerPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        val themePreference = ThemePreference.getInstance(requireActivity().application.dataStore)
        val themeViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(themePreference, requireActivity().application)
        )[ThemeViewModel::class.java]

        themeViewModel.getThemeSettings().observe(viewLifecycleOwner) {
            checkDarkSetting(it)
        }

        binding.switchTheme.setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
            themeViewModel.setThemeSetting(isChecked)
        }

        themeViewModel.getReminderSettings().observe(viewLifecycleOwner) {
            isChecked(it)
        }


        binding.setNotification.setOnCheckedChangeListener { _, isChecked ->
            themeViewModel.setReminderSetting(isChecked)
            if (isChecked) {
                themeViewModel.startReminder()
            } else {
                themeViewModel.stopReminder()
            }
        }
    }

    private fun checkDarkSetting(isDark: Boolean) {
        binding.switchTheme.isChecked = isDark
    }

    private fun isChecked(isChecked: Boolean) {
        binding.setNotification.isChecked = isChecked
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

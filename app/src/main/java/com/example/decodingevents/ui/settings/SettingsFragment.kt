package com.example.decodingevents.ui.settings

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.decodingevents.R
import com.example.decodingevents.data.factory.ViewModelFactory
import com.example.decodingevents.data.preference.ThemePreference
import com.example.decodingevents.data.preference.dataStore
import com.example.decodingevents.databinding.FragmentSettingsBinding
import com.example.decodingevents.util.NotificationWorker
import java.util.concurrent.TimeUnit

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val workManager by lazy { WorkManager.getInstance(requireActivity()) }

    private var _periodicWorkRequest: PeriodicWorkRequest? = null

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

        // Request notification permission if API level is 33 or higher
        if (Build.VERSION.SDK_INT >= 33) {
            registerPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        val themePreference = ThemePreference.getInstance(requireActivity().application.dataStore)
        val themeViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(themePreference)
        )[ThemeViewModel::class.java]

        themeViewModel.getThemeSettings().observe(viewLifecycleOwner) {
            checkDarkSetting(it)
        }

        binding.switchTheme.setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
            themeViewModel.setThemeSetting(isChecked)
        }

        // Set up notification switch listener
        binding.setNotification.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                startPeriodicTable()
            } else {
                cancelPeriodicTable()
            }
        }
    }

    private fun checkDarkSetting(isDark: Boolean) {
        binding.switchTheme.isChecked = isDark
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startPeriodicTable() {
        val constraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        _periodicWorkRequest =
            PeriodicWorkRequest.Builder(NotificationWorker::class.java, 1, TimeUnit.DAYS)
                .setConstraints(constraint)
                .build()

        _periodicWorkRequest?.let { request ->
            workManager.enqueue(request)

            // Observe the work status
            workManager.getWorkInfoByIdLiveData(request.id)
                .observe(viewLifecycleOwner) { workInfo ->
                    if (workInfo != null) {
                        Log.d("SettingFragment", workInfo.toString())
                    }
                }
        }
    }

    private fun cancelPeriodicTable() {
        _periodicWorkRequest?.let {
            workManager.cancelWorkById(it.id)
        }
    }
}

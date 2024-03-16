package com.example.weathery.main.settings.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.weathery.R
import com.example.weathery.data.database.FavLocationLocalDataSourceImpl
import com.example.weathery.data.models.Settings
import com.example.weathery.data.network.WeatherRemoteDataSourceImpl
import com.example.weathery.data.repositories.SettingsRepositoryImpl
import com.example.weathery.data.repositories.WeatherRepositoryImpl
import com.example.weathery.data.sharedpreferences.SettingsLocalDataSourceImpl
import com.example.weathery.databinding.FragmentSettingsBinding
import com.example.weathery.main.shared.SettingsViewModel
import com.example.weathery.main.shared.SettingsViewModelFactory
import com.example.weathery.main.shared.WeatherViewModel
import com.example.weathery.main.shared.WeatherViewModelFactory
import com.example.weathery.utils.Constants
import com.example.weathery.utils.Constants.Companion.LANG_ARABIC_KEY
import com.example.weathery.utils.Constants.Companion.LANG_ENGLISH_KEY
import com.example.weathery.utils.Constants.Companion.UNITS_IMPERIAL_KEY
import com.example.weathery.utils.Constants.Companion.UNITS_METRIC_KEY
import com.example.weathery.utils.Constants.Companion.UNITS_STANDARD_KEY
import com.example.weathery.utils.LocaleHelper

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var settingsViewModelFactory: SettingsViewModelFactory
    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var language: String
    private lateinit var units: String
    private var isNotificationEnabled = false



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        setupViewModel()
        setupUnitRadioGroup()
        setupLangRadioGroup()
        setupNotificationSwitch()
        setupSaveFab()
        return binding.root
    }



    private fun setupLangRadioGroup(){
        binding.radioGpLang.setOnCheckedChangeListener { group, checkedId ->
            val radioButton = binding.root.findViewById<RadioButton>(checkedId)
            when (radioButton.text) {
                getString(R.string.english) -> {
                    language = LANG_ENGLISH_KEY
                }
                getString(R.string.arabic) -> {
                    language =  LANG_ARABIC_KEY
                }
            }
        }
        language = settingsViewModel.getLanguage()
        when(language){
            LANG_ENGLISH_KEY -> {
                binding.radioGpLang.check(binding.radioButtonEn.id)
            }
            LANG_ARABIC_KEY -> {
                binding.radioGpLang.check(binding.radioButtonAr.id)
            }
        }
    }

    private fun setupUnitRadioGroup() {
        binding.radioGpUnits.setOnCheckedChangeListener { group, checkedId ->
            val radioButton = binding.root.findViewById<RadioButton>(checkedId)
            when (radioButton.text) {
                getString(R.string.standard) -> {
                    units = UNITS_STANDARD_KEY
                }
                getString(R.string.metric) -> {
                    units = UNITS_METRIC_KEY
                }
                getString(R.string.imperial) -> {
                    units = UNITS_IMPERIAL_KEY
                }
            }
        }
        units = settingsViewModel.getUnits()
        when(units){
            UNITS_STANDARD_KEY -> {
                binding.radioGpUnits.check(binding.radioButtonStandard.id)
            }
            UNITS_METRIC_KEY -> {
                binding.radioGpUnits.check(binding.radioButtonMetric.id)
            }
            UNITS_IMPERIAL_KEY -> {
                binding.radioGpUnits.check(binding.radioButtonImperial.id)
            }
        }
    }

    private fun setupViewModel(){
        settingsViewModelFactory = SettingsViewModelFactory(
            SettingsRepositoryImpl.
            getInstance(SettingsLocalDataSourceImpl.getInstance(requireContext())))
        settingsViewModel = ViewModelProvider(this as ViewModelStoreOwner,
            settingsViewModelFactory)[SettingsViewModel::class.java]
    }

    private fun setupNotificationSwitch(){
        isNotificationEnabled = settingsViewModel.isNotificationEnabled()
        binding.switchNotification.isChecked = isNotificationEnabled

        binding.switchNotification.setOnCheckedChangeListener { _, isChecked ->
            isNotificationEnabled = isChecked
        }

    }

    private fun setupSaveFab(){
        binding.fabSave.setOnClickListener {
            settingsViewModel.saveSettings(Settings(language,
                isNotificationEnabled,
                units))
            LocaleHelper.changeLanguageLocaleTo(language)
            Toast.makeText(requireContext(), getString(R.string.changes_saved),Toast.LENGTH_SHORT).show()
        }
    }


}
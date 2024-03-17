package com.example.weathery.main.shared

import com.example.weathery.data.repositories.FakeSettingsRepository
import com.example.weathery.data.repositories.SettingsRepository
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test


class SettingsViewModelTest{

    lateinit var viewModel: SettingsViewModel
    lateinit var repo: SettingsRepository
    @Before
    fun setup(){
        repo = FakeSettingsRepository()
        viewModel = SettingsViewModel(repo)
    }

    @Test
    fun getLanguage_returnsEnglish(){

        //when
        val result = viewModel.getLanguage()

        //then
        assertThat(result, `is`("english"))
    }

    @Test
    fun setLanguage_stringLanguage_returnsLanguage(){

        //when
        val arabicLang = "arabic"
        viewModel.setLanguage(arabicLang)
        val result = viewModel.getLanguage()

        //then
        assertThat(result, `is`(arabicLang))
    }

    @Test
    fun getUnits_returnsMetric(){

        //when
        val result = viewModel.getUnits()

        //then
        assertThat(result, `is`("metric"))
    }

    @Test
    fun setUnits_stringUnit_returnsUnit(){

        //when
        val metricUnit = "metric"
        viewModel.setLanguage(metricUnit)
        val result = viewModel.getUnits()

        //then
        assertThat(result, `is`(metricUnit))
    }

    @Test
    fun setNotificationEnabled_booleanEnabled_returnsEnabled(){

        //when
        val enabled = true
        viewModel.setNotificationEnabled(enabled)
        val result = viewModel.isNotificationEnabled()

        //then
        assertThat(result, `is`(enabled))
    }


}
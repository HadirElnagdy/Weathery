package com.example.weathery.main.shared

import android.app.Application
import android.location.Location
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weathery.data.models.FavLocationsWeather
import com.example.weathery.data.models.WeatherResponse
import com.example.weathery.data.repositories.FakeWeatherRepository
import com.example.weathery.data.repositories.WeatherRepository
import com.example.weathery.utils.ApiState
import getOrAwaitValue
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SharedViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    lateinit var viewModel: SharedViewModel
    lateinit var repo: WeatherRepository

    //getAllFav addToFav delFromFav getForecast getCityName
    @Before
    fun setUp() {
        val app = ApplicationProvider.getApplicationContext() as Application
        repo = FakeWeatherRepository()
        viewModel = SharedViewModel(app, repo)
    }





}
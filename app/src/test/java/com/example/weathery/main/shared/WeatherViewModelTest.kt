package com.example.weathery.main.shared

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weathery.data.repositories.FakeWeatherRepository
import com.example.weathery.data.repositories.WeatherRepository
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WeatherViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    lateinit var viewModel: WeatherViewModel
    lateinit var repo: WeatherRepository

    //getAllFav addToFav delFromFav getForecast getCityName
    @Before
    fun setUp() {
        val app = ApplicationProvider.getApplicationContext() as Application
        repo = FakeWeatherRepository()
        viewModel = WeatherViewModel(app, repo)
    }

    @Test
    fun getAllFavs_returnsWeatherResponse() = runBlockingTest{
        viewModel.getAllFavs()
    }

            /*fun getAllFavs() = viewModelScope.launch(Dispatchers.IO) {
        repository.getAllFavorites().collectLatest{
            _mutableFavList.postValue(it)
        }
    }*/




}
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
import com.example.weathery.data.utils.ApiState
import getOrAwaitValue
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
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

    @Before
    fun setUp() {
        val app = ApplicationProvider.getApplicationContext() as Application
        repo = FakeWeatherRepository()
        viewModel = WeatherViewModel(app, repo)
    }

    @Test
    fun getForecast_location_returnsNotNullResult() = runBlockingTest{
        //Given
        var location = Location("Test")
        location.longitude = 30.01234
        location.latitude = 31.01234

        //when
        viewModel.getForecast(location)

        //then
        val result = viewModel.forecast.value
        assertThat(result, `is`(notNullValue()))
    }

    @Test
    fun getAllFavs_location_returnsWeatherResponseWithSameLocation() = runBlockingTest{
        //Given
        var location = Location("Test")
        location.longitude = 30.01234
        location.latitude = 31.01234
        viewModel.addToFav(location)

        //when
        viewModel.getAllFavs()

        //then
        val result = viewModel.favList.getOrAwaitValue {  }
        assertThat(result, not(nullValue()))

    }




}
package com.example.weathery.data.repositories

import com.example.weathery.data.database.FakeFavLocalDataSource
import com.example.weathery.data.database.FavLocationLocalDataSource
import com.example.weathery.data.models.FavLocationsWeather
import com.example.weathery.data.models.WeatherResponse
import com.example.weathery.data.network.FakeWeatherRemoteDataSource
import com.example.weathery.data.network.WeatherRemoteDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

class WeatherRepositoryImplTest {

    lateinit var repo: WeatherRepository
    lateinit var localDataSource: FavLocationLocalDataSource
    lateinit var remoteDataSource: WeatherRemoteDataSource
    @Before
    fun setup(){
        localDataSource = FakeFavLocalDataSource()
        remoteDataSource = FakeWeatherRemoteDataSource()
        repo = WeatherRepositoryImpl.getInstance(localDataSource, remoteDataSource)
    }

    @Test
    fun getWeatherForecast_longitudeLatitude_returnsFlowOfWeatherResponse() = runBlockingTest{
        //when
        val result = repo.getWeatherForecast(30.01234, 32.01234).first()

        //then
        val value = WeatherResponse(lon = 30.01234, lat = 32.01234)
        assertThat(result, `is`(value))
    }

    @Test
    fun insertAndGetAllFavorites_favLocationWeather_returnsTheInsertedFav() = runBlockingTest {
        //Given
        val favLocationsWeather = FavLocationsWeather("Ismailia", WeatherResponse(lon = 30.01234, lat = 32.01234))

        //when: insert(fav), getAllFav
        repo.insertFavorite(favLocationsWeather)
        val result = repo.getAllFavorites().first().get(0)

        //then
        assertThat(result, `is`(favLocationsWeather))

    }

    @Test
    fun deleteFavoriteAndGetFavorite_returnsEmptyList() = runBlockingTest {
        //Given
        val favLocationsWeather = FavLocationsWeather("Ismailia", WeatherResponse(lon = 30.01234, lat = 32.01234))

        //when: insert(fav), delete(fav)
        repo.insertFavorite(favLocationsWeather)
        repo.deleteFavorite(favLocationsWeather)
        val result = repo.getAllFavorites().first().isEmpty()

        //then
        assertThat(result, `is`(true))
    }

}
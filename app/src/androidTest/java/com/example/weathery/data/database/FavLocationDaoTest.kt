package com.example.weathery.data.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.weathery.data.models.FavLocationsWeather
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class FavLocationDaoTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private lateinit var database: WeatherDatabase

    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java)
            .build()
    }

    @Test
    fun insertFavAndGetFav_favLocationWeather_getTheInsertedLocation() = runBlockingTest{

        //Given
        val fav = FavLocationsWeather("Cairo Governorate")
        database.getFavDao().insertFavorite(fav)

        //When
        val loaded = database.getFavDao().getAllFavorites().first().get(0)

        //Then
        assertThat(loaded, notNullValue())
        assertThat(loaded, `is`(fav))
    }

    @Test
    fun deleteProductAndGetProduct() = runBlockingTest {

       //Given
        val fav = FavLocationsWeather("Cairo Governorate")
        database.getFavDao().insertFavorite(fav)
        database.getFavDao().deleteFavorite(fav)

        //When
        val result = database.getFavDao().getAllFavorites().first().isEmpty()

        //Then
        assertThat(result, IsEqual(true))

    }

    @After
    fun tearDown() = database.close()

}
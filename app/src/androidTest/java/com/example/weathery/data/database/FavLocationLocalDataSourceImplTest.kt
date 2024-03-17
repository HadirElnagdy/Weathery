package com.example.weathery.data.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import androidx.test.filters.MediumTest
import androidx.test.filters.SmallTest
import com.example.weathery.data.models.FavLocationsWeather
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.core.IsEqual
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class FavLocationLocalDataSourceImplTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private lateinit var localDataSource: FavLocationLocalDataSource

    @Before
    fun setup(){
        localDataSource = FavLocationLocalDataSourceImpl.getInstance(getApplicationContext())
    }

    @Test
    fun insertFavAndGetFav_favLocationWeather_getTheInsertedLocation() = runBlockingTest{

        //Given
        val fav = FavLocationsWeather("Cairo Governorate")
        localDataSource.insertFavorite(fav)

        //When
        val loaded = localDataSource.getAllFavorites().first().get(0)

        //Then
        MatcherAssert.assertThat(loaded, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(loaded, CoreMatchers.`is`(fav))
    }

    @Test
    fun deleteFavoriteAndGetFavorite() = runBlockingTest {

        //Given
        val fav = FavLocationsWeather("Cairo Governorate")
        localDataSource.insertFavorite(fav)
        localDataSource.deleteFavorite(fav)

        //When
        val result = localDataSource.getAllFavorites().first().isEmpty()

        //Then
        MatcherAssert.assertThat(result, IsEqual(true))
    }


}
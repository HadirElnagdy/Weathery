package com.example.weathery.data.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import androidx.test.filters.MediumTest
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
@MediumTest
class FavLocationLocalDataSourceImplTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private lateinit var localDataSource: FavLocationLocalDataSource
    private lateinit var database: WeatherDatabase

    @Before
    fun setup(){
        //localDataSource = /*postponed until we finish di*/
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java)
            .build()
    }



    @After
    fun tearDown() = database.close()
}

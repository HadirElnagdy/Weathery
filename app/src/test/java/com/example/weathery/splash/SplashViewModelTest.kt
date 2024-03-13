package com.example.weathery.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weathery.data.repositories.WeatherRepository
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SplashViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    lateinit var viewModel: SplashViewModel

    @Before
    fun setUp() {
        viewModel = SplashViewModel()
    }




}


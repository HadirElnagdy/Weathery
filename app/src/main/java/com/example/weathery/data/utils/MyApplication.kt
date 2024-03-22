package com.example.weathery.data.utils

import android.app.Application
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

class MyApplication : Application(), ViewModelStoreOwner {
    @get:JvmName("getAppViewModelStore")
    private val appViewModelStore: ViewModelStore by lazy {
        ViewModelStore()
    }
    override val viewModelStore: ViewModelStore
        get() = appViewModelStore
}

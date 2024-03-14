package com.example.weathery.main.shared

import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.weathery.R
import com.example.weathery.data.database.FavLocationLocalDataSourceImpl
import com.example.weathery.data.network.WeatherRemoteDataSourceImpl
import com.example.weathery.databinding.ActivityMainBinding
import com.example.weathery.data.repositories.WeatherRepositoryImpl


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    lateinit var viewModelFactory: WeatherViewModelFactory
    lateinit var viewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModelFactory = WeatherViewModelFactory(this.application
            , WeatherRepositoryImpl.getInstance(FavLocationLocalDataSourceImpl.getInstance(this),
                WeatherRemoteDataSourceImpl))
        viewModel = ViewModelProvider(this as ViewModelStoreOwner, viewModelFactory).get(WeatherViewModel::class.java)

        val location = intent.getParcelableExtra("location") as? Location
        Log.i(TAG, "onCreate: ${location.toString()}")
        viewModel.getForecast(location)
        val actionBar = supportActionBar
        actionBar?.let {
            it.setHomeAsUpIndicator(R.drawable.ic_menu)
            it.setDisplayShowHomeEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowTitleEnabled(false)
            it.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.transparent)))
        }

        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(binding.navigationLayout, navController)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        return super.onOptionsItemSelected(item)
    }


}
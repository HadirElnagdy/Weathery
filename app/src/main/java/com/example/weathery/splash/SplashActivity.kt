package com.example.weathery.splash

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weathery.R
import com.example.weathery.main.shared.MainActivity
import com.example.weathery.utils.Constants
import com.example.weathery.utils.LocationState
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.log

class SplashActivity : AppCompatActivity() {

    private val TAG = "SplashActivity"
    lateinit var splashViewModel : SplashViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        splashViewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
        Log.i(TAG, "onCreate: ")
        supportActionBar?.hide()

    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume: ")
        getLocationWithPermissions()
    }

    private fun checkPermissions(): Boolean {
        Log.i(TAG, "checkPermissions: ")
        val result = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
        return result
    }

    private fun getLocationWithPermissions(){
        Log.i(TAG, "getLocationWithPermissions: ")
        if(checkPermissions()){
            if(isLocationEnabled()){
                splashViewModel.getLocation(client = LocationServices.getFusedLocationProviderClient(this))
                updateUI()
            }else{
                Toast.makeText(this, "Please turn on your location first", Toast.LENGTH_SHORT).show()
                val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(myIntent)
            }
        }else{
            requestPermission()
        }
    }

    private fun isLocationEnabled(): Boolean {
        Log.i(TAG, "isLocationEnabled: ")
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun requestPermission(){
        Log.i(TAG, "requestPermission: ")
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION), Constants.PERMISSION_ID)
    }

    private fun updateUI() {
        Log.i(TAG, "updateUI: ")
        lifecycleScope.launch {
            splashViewModel.locationStateFlow.collectLatest {
                when(it){
                    is LocationState.Failure -> {
                        Log.i(TAG, "updateUI: ${it.msg}")
                    }
                    LocationState.Loading -> {}
                    is LocationState.Success -> {
                        val intent = Intent(this@SplashActivity, MainActivity::class.java).apply {
                            Log.i(TAG, "updateUI: ${it.data.toString()}")
                            putExtra("location", it.data)
                        }
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }



}
package com.example.weathery.main.map

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weathery.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar


class MapFragment : Fragment() , OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private val TAG = "MapFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return view
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.setOnMapClickListener { latLng ->
            val markerOptions: MarkerOptions = MarkerOptions()
            markerOptions.position(latLng)
            markerOptions.title("${latLng.latitude} KG ${latLng.longitude}")
            googleMap.clear()
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10F))
            googleMap.addMarker(markerOptions)
            showSaveLocationSnackbar(latLng)
        }
    }

    private fun showSaveLocationSnackbar(latLng: LatLng) {
        Snackbar.make(
            requireView(),
            "Do you want to save this location?",
            Snackbar.LENGTH_LONG
        ).setAction("Save") {
            // Code to save the location
            // You can access the latitude and longitude from latLng variable
            // For example, you can store them in ViewModel or Repository
            Log.i(TAG, "showSaveLocationSnackbar: lat: ${latLng.latitude}")
        }.show()
    }

}
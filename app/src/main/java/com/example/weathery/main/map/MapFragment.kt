package com.example.weathery.main.map

import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import com.example.weathery.R
import com.example.weathery.database.FavLocationLocalDataSourceImpl
import com.example.weathery.main.shared.SharedViewModel
import com.example.weathery.main.shared.SharedViewModelFactory
import com.example.weathery.models.WeatherRepositoryImpl
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar


class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    lateinit var viewModelFactory: SharedViewModelFactory
    lateinit var viewModel: SharedViewModel
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
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
            Snackbar.LENGTH_INDEFINITE
        ).setAction("Save") {
            val fromFav = MapFragmentArgs.fromBundle(requireArguments()).fromFav
            if (fromFav) {
                val location = Location("Fav Location")
                location.latitude = latLng.latitude
                location.longitude = latLng.longitude
                viewModel.addToFav(location)
                findNavController().popBackStack()
                Log.i(TAG, "Added To Favs")
            }else{
                Log.i(TAG, "showSaveLocationSnackbar: From settings")
            }
            Log.i(TAG, "showSaveLocationSnackbar: lat: ${latLng.latitude}")
        }.show()
    }

    private fun setupViewModel(){
        viewModelFactory = SharedViewModelFactory(requireActivity().application,
            WeatherRepositoryImpl.getInstance(FavLocationLocalDataSourceImpl.getInstance(requireContext())))
        viewModel = ViewModelProvider(activity as ViewModelStoreOwner, viewModelFactory).get(SharedViewModel::class.java)
    }

}
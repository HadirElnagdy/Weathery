package com.example.weathery.main.favorites.view

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weathery.R
import com.example.weathery.data.database.FavLocationLocalDataSourceImpl
import com.example.weathery.data.models.WeatherResponse
import com.example.weathery.data.network.WeatherRemoteDataSourceImpl
import com.example.weathery.data.repositories.SettingsRepositoryImpl
import com.example.weathery.data.repositories.WeatherRepositoryImpl
import com.example.weathery.data.sharedpreferences.SettingsLocalDataSourceImpl
import com.example.weathery.databinding.FragmentFavoritesDetailsBinding
import com.example.weathery.main.shared.SettingsViewModel
import com.example.weathery.main.shared.SettingsViewModelFactory
import com.example.weathery.main.shared.WeatherViewModel
import com.example.weathery.main.shared.WeatherViewModelFactory
import com.example.weathery.main.weather.view.DailyRecyclerViewAdapter
import com.example.weathery.main.weather.view.HourlyRecyclerViewAdapter
import com.example.weathery.data.utils.ApiState
import com.example.weathery.data.utils.Constants
import com.example.weathery.data.utils.NetworkUtils
import com.example.weathery.data.utils.SimpleUtils
import kotlinx.coroutines.launch


class FavoritesDetailsFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesDetailsBinding
    private lateinit var viewModelFactory: WeatherViewModelFactory
    private lateinit var viewModel: WeatherViewModel
    private lateinit var settingsViewModelFactory: SettingsViewModelFactory
    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var hourlyAdapter: HourlyRecyclerViewAdapter
    private lateinit var dailyAdapter: DailyRecyclerViewAdapter
    private lateinit var response: WeatherResponse
    private lateinit var location: Location
    private val tag = "WeatherFragment"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFavoritesDetailsBinding.inflate(inflater, container, false)
        setupViewModel()
        getForecast()
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(NetworkUtils.isNetworkAvailable(requireContext())) {
            observeWeatherForecast()
        }else {
            Log.i(tag, "onViewCreated: No Network here!!!")
        }

    }



    private fun observeWeatherForecast(){
        lifecycleScope.launch {
            viewModel.favForecast.collect { state ->
                when (state) {
                    is ApiState.Loading -> showLoadingState()
                    is ApiState.Success -> updateUI(state.data)
                    is ApiState.Failure -> showErrorState(state.msg.toString())
                }
            }
        }
    }

    private fun showErrorState(msg: String){
        binding.progressBar2.visibility = View.INVISIBLE
        binding.scrollView4.visibility = View.INVISIBLE
        Log.i(tag, "setUpUI: ${msg}")
    }

    private fun showLoadingState(){
        binding.progressBar2.visibility = View.VISIBLE
        binding.scrollView4.visibility = View.INVISIBLE
    }
    @SuppressLint("SetTextI18n")
    private fun updateUI(data: WeatherResponse){
        response = data
        val current = data.current?.weather?.get(0)
        Log.i(tag, "updateUI: $data")
        binding.progressBar2.visibility = View.INVISIBLE
        binding.scrollView4.visibility = View.VISIBLE
        binding.txtFavCity.text = viewModel.getAdminArea(location)
        binding.txtFavTemp.text = "${data.current?.temp}\u00B0"
        binding.txtFavDesc.text = "${current?.main}"
        binding.imgFavIcon.setImageResource(SimpleUtils.getIconResourceId(current?.icon?:""))
        var dateTime = SimpleUtils.convertUnixTimeStamp(data.current?.dt?.toLong(),
            data.timezone)
        binding.txtFavTime.text = "${getString(R.string.last_update)}: ${dateTime.first} ${dateTime.second}"
        binding.txtFavHumidity.text = "${data.current?.humidity?.toString()}%"
        binding.txtFavPressure.text = data.current?.pressure?.toString()
        binding.txtFavVisibility.text = data.current?.visibility?.toString()
        binding.txtWindSpeedFav.text = data.current?.wind_speed?.toString()
        setupRecyclerViews(data.timezone!!)
    }

    private fun setupViewModel(){
        viewModelFactory = WeatherViewModelFactory(requireActivity().application,
            WeatherRepositoryImpl.getInstance(
                FavLocationLocalDataSourceImpl.getInstance(requireContext()),
                WeatherRemoteDataSourceImpl
            ))
        viewModel = ViewModelProvider(activity as ViewModelStoreOwner, viewModelFactory).get(
            WeatherViewModel::class.java)
        settingsViewModelFactory = SettingsViewModelFactory(
            SettingsRepositoryImpl
            .getInstance(SettingsLocalDataSourceImpl.getInstance(requireContext())))
        settingsViewModel = ViewModelProvider(activity as ViewModelStoreOwner, settingsViewModelFactory)
            .get(SettingsViewModel::class.java)

    }
    private fun setupRecyclerViews(timeZone: String){
        hourlyAdapter = HourlyRecyclerViewAdapter(requireContext())
        hourlyAdapter.setTimeZone(timeZone)
        hourlyAdapter.submitList(response.hourly)
        binding.recyclerViewHourlyFav.apply {
            val linearLayout = LinearLayoutManager(context)
            linearLayout.orientation = RecyclerView.HORIZONTAL
            layoutManager = linearLayout
            adapter = hourlyAdapter
        }
        dailyAdapter = DailyRecyclerViewAdapter(requireContext()){ }
        dailyAdapter.submitList(response.daily)
        binding.recyclerViewDailyFav.apply {
            val linearLayout = LinearLayoutManager(context)
            linearLayout.orientation = RecyclerView.VERTICAL
            layoutManager = linearLayout
            adapter = dailyAdapter
        }
    }

    private fun getForecast(){
        location = FavoritesDetailsFragmentArgs.fromBundle(requireArguments()).location!!
        val units = if(settingsViewModel.getUnits() == Constants.UNITS_STANDARD_KEY) null else settingsViewModel.getUnits()
        viewModel.getFavForecast(location = location,
            language = settingsViewModel.getLanguage(),
            units = units)
    }
}
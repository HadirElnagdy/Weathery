package com.example.weathery.main.weather.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weathery.R
import com.example.weathery.data.database.FavLocationLocalDataSourceImpl
import com.example.weathery.databinding.FragmentWeatherBinding
import com.example.weathery.main.shared.SharedViewModel
import com.example.weathery.main.shared.SharedViewModelFactory
import com.example.weathery.data.repositories.WeatherRepositoryImpl
import com.example.weathery.utils.ApiState
import com.example.weathery.utils.NetworkUtils
import com.example.weathery.data.models.WeatherResponse
import com.example.weathery.utils.SimpleUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class WeatherFragment : Fragment() {

    private lateinit var binding: FragmentWeatherBinding
    private lateinit var viewModelFactory: SharedViewModelFactory
    private lateinit var viewModel: SharedViewModel
    private lateinit var hourlyAdapter: HourlyRecyclerViewAdapter
    private lateinit var dailyAdapter: DailyRecyclerViewAdapter
    private lateinit var response: WeatherResponse
    private val tag = "WeatherFragment"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        if(NetworkUtils.isNetworkAvailable(requireContext())) {
            observeWeatherForecast()
        }else {
            Log.i(tag, "onViewCreated: No Network here!!!")
        }

    }

    private fun observeWeatherForecast(){
        lifecycleScope.launch {
            viewModel.forecast.collectLatest { state ->
                when (state) {
                    is ApiState.Loading -> showLoadingState()
                    is ApiState.Success -> updateUI(state.data)
                    is ApiState.Failure -> showErrorState(state.msg.toString())
                }
            }
        }
    }
    private fun showErrorState(msg: String){
        binding.progressBar.visibility = View.INVISIBLE
        binding.scrollView3.visibility = View.INVISIBLE
        Log.i(tag, "setUpUI: ${msg}")
        //if msg == can't find api update the ui with the existing room weather
    }

    private fun showLoadingState(){
        binding.progressBar.visibility = View.VISIBLE
        binding.scrollView3.visibility = View.INVISIBLE
    }
    @SuppressLint("SetTextI18n")
    private fun updateUI(data: WeatherResponse){
        response = data
        val current = data.current?.weather?.get(0)
        binding.progressBar.visibility = View.INVISIBLE
        binding.scrollView3.visibility = View.VISIBLE
        binding.txtCityName.text = viewModel.getAdminArea(viewModel.location)
        binding.txtTemp.text = "${data.current?.temp}\u00B0"
        binding.txtWeatherDesc.text = "${current?.main}"
        binding.imgIcon.setImageResource(SimpleUtils.getIconResourceId(current?.icon?:""))
        var dateTime = SimpleUtils.convertUnixTimeStamp(data.current?.dt?.toLong(),
            data.timezone)
        binding.txtTime.text = "${getString(R.string.last_update)}: ${dateTime.first} ${dateTime.second}"

        Log.i(tag, SimpleUtils.convertUnixTimeStamp(data.current?.dt?.toLong(),
            data.timezone).toString())
        binding.txtHumidity.text = data.current?.humidity?.toString()
        binding.txtPressure.text = data.current?.pressure?.toString()
        binding.txtVisibility.text = data.current?.visibility?.toString()
        binding.txtWindSpeed.text = data.current?.windSpeed?.toString()
        Log.i(tag, "updateUI: ${data.current?.windSpeed?.toString()}")
        data.current?.weather?.get(0)?.id?.let {
          /*  if(it >= 300 && it <= 531){
                binding.lottieHome.setAnimation(R.raw.rain_animation)
                binding.lottieHome.visibility = View.VISIBLE
            }
            else if(it >= 600 && it <= 622){
                binding.lottieHome.setAnimation(R.raw.snow_animation)
                binding.lottieHome.visibility = View.VISIBLE
            }
            else{
                binding.lottieHome.visibility = View.INVISIBLE
            }*/
        }
        setupRecyclerViews(data.timezone!!)
    }

    private fun setupViewModel(){
        viewModelFactory = SharedViewModelFactory(requireActivity().application,
            WeatherRepositoryImpl.getInstance(FavLocationLocalDataSourceImpl.getInstance(requireContext())))
        viewModel = ViewModelProvider(activity as ViewModelStoreOwner, viewModelFactory).get(SharedViewModel::class.java)
    }
    private fun setupRecyclerViews(timeZone: String){
        hourlyAdapter = HourlyRecyclerViewAdapter(requireContext())
        hourlyAdapter.setTimeZone(timeZone)
        hourlyAdapter.submitList(response.hourly)
        binding.recyclerViewHourly.apply {
            val linearLayout = LinearLayoutManager(context)
            linearLayout.orientation = RecyclerView.HORIZONTAL
            layoutManager = linearLayout
            adapter = hourlyAdapter
        }

        dailyAdapter = DailyRecyclerViewAdapter(requireContext()){
            //open the day
        }

        dailyAdapter.submitList(response.daily)
        binding.recyclerViewDaily.apply {
            val linearLayout = LinearLayoutManager(context)
            linearLayout.orientation = RecyclerView.VERTICAL
            layoutManager = linearLayout
            adapter = dailyAdapter
        }
    }
}
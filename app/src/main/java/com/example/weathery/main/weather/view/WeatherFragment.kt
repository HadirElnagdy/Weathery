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
import com.bumptech.glide.Glide
import com.example.weathery.R
import com.example.weathery.databinding.FragmentWeatherBinding
import com.example.weathery.main.shared.SharedViewModel
import com.example.weathery.main.shared.SharedViewModelFactory
import com.example.weathery.models.WeatherRepositoryImpl
import com.example.weathery.utils.ApiState
import com.example.weathery.utils.NetworkUtils
import com.example.weathery.models.WeatherResponse
import com.example.weathery.utils.DateTimeService
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class WeatherFragment : Fragment() {

    lateinit var binding: FragmentWeatherBinding
    lateinit var viewModelFactory: SharedViewModelFactory
    lateinit var viewModel: SharedViewModel
    lateinit var hourlyAdapter: HourlyRecyclerViewAdapter
    lateinit var response: WeatherResponse
    private val TAG = "WeatherFragment"



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

        viewModelFactory = SharedViewModelFactory(requireContext(), WeatherRepositoryImpl)
        viewModel = ViewModelProvider(activity as ViewModelStoreOwner, viewModelFactory).get(SharedViewModel::class.java)
        if(NetworkUtils.isNetworkAvailable(requireContext())) {
            setUpUI()
        }else {
            Log.i(TAG, "onViewCreated: No Network here!!!")
        }


    }

    private fun setUpUI(){
        lifecycleScope.launch {
            viewModel.forecast.collectLatest {
                when (it) {
                    is ApiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.scrollView3.visibility = View.INVISIBLE
                    }
                    is ApiState.Success -> {
                        updateUI(it.data)
                    }
                    is ApiState.Failure -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        Log.i(TAG, "onFailure: ${it.msg}")
                        //Custom alert with error message
                    }
                }
            }

        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(data: WeatherResponse){
        response = data
        binding.progressBar.visibility = View.INVISIBLE
        binding.scrollView3.visibility = View.VISIBLE
        binding.txtCityName.text = viewModel.getCityName()
        binding.txtTemp.text = "${data.current?.temp}\u00B0"
        binding.txtWeatherDesc.text = "${data.current?.weather?.get(0)?.main}"
        Glide.with(requireContext())
            .load("https://openweathermap.org/img/wn/${data.current?.weather?.get(0)?.icon}@2x.png")
            .centerCrop()
            .into(binding.imgIcon)
        binding.txtTime.text = "${getString(R.string.last_update)}: ${DateTimeService.currentDateTime}"
        setupRecyclerViews()

    }

    private fun setupRecyclerViews(){
        hourlyAdapter = HourlyRecyclerViewAdapter(requireContext())
        hourlyAdapter.submitList(response.hourly)
        binding.recyclerViewHourly.apply {
            val linearLayout = LinearLayoutManager(context)
            linearLayout.orientation = RecyclerView.HORIZONTAL
            layoutManager = linearLayout
            adapter = hourlyAdapter
        }
    }
}
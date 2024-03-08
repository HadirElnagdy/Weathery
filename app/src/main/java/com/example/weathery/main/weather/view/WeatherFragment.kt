package com.example.weathery.main.weather.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.weathery.databinding.FragmentWeatherBinding
import com.example.weathery.models.WeatherRepositoryImpl
import com.example.weathery.utils.ApiState
import com.example.weathery.utils.NetworkUtils
import com.example.weathery.main.weather.viewmodel.WeatherViewModel
import com.example.weathery.main.weather.viewmodel.WeatherViewModelFactory
import com.example.weathery.models.WeatherResponse
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class WeatherFragment : Fragment() {

    lateinit var binding: FragmentWeatherBinding
    lateinit var viewModelFactory: WeatherViewModelFactory
    val viewModel: WeatherViewModel by viewModels { viewModelFactory }
    private val TAG = "WeatherFragment"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
/*

        viewModelFactory = WeatherViewModelFactory(WeatherRepositoryImpl)
        if(NetworkUtils.isNetworkAvailable(requireContext())) {
            viewModel.getForcast(123.456, 78.90)
            setUpUI()
        }else {
            Log.i(TAG, "onViewCreated: No Network here!!!")
        }

*/

    }

    private fun setUpUI(){
        lifecycleScope.launch {
            viewModel.forecast.collectLatest {
                when (it) {
                    is ApiState.Loading -> {
                        //progress bar
                    }
                    is ApiState.Success -> {
                        Log.i(TAG, "onSuccess: ${it.data}")
                        updateUI(it.data)
                    }

                    is ApiState.Failure -> {
                        Log.i(TAG, "onFailure: ${it.msg}")
                        //Custom alert with error message
                    }
                }
            }

        }
    }

    private fun updateUI(data: WeatherResponse){
        //progress bar invisible
        //set the data
    }
}
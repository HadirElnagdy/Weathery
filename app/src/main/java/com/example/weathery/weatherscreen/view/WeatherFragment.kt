package com.example.weathery.weatherscreen.view

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
import com.example.weathery.weatherscreen.viewmodel.WeatherViewModel
import com.example.weathery.weatherscreen.viewmodel.WeatherViewModelFactory
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

        viewModelFactory = WeatherViewModelFactory(WeatherRepositoryImpl)
        if(NetworkUtils.isNetworkAvailable(requireContext())) {
            viewModel.getForcast(123.456, 78.90)

            lifecycleScope.launch {
                viewModel.stateFlow.collectLatest {
                    when (it) {
                        is ApiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is ApiState.Success -> {
                            binding.progressBar.visibility = View.INVISIBLE
//                        updateUI()
                            Log.i(TAG, "onSuccess: ${it.data}")
                        }

                        is ApiState.Failure -> {
                            binding.progressBar.visibility = View.INVISIBLE
                            Log.i(TAG, "onFailure: ${it.msg}")
                        }
                    }
                }


            }
        }else {
            Log.i(TAG, "onViewCreated: No Network here!!!")
        }
    }
}
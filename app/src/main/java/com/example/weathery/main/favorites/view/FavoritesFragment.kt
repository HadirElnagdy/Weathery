package com.example.weathery.main.favorites.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weathery.R
import com.example.weathery.database.FavLocationLocalDataSourceImpl
import com.example.weathery.databinding.FragmentFavoritesBinding
import com.example.weathery.main.shared.SharedViewModel
import com.example.weathery.main.shared.SharedViewModelFactory
import com.example.weathery.models.FavLocationsWeather
import com.example.weathery.models.WeatherRepositoryImpl


class FavoritesFragment : Fragment() {

    lateinit var binding: FragmentFavoritesBinding
    lateinit var viewModelFactory: SharedViewModelFactory
    lateinit var viewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        viewModel.getAllFavs()
        setupRecyclerView()
        setupFabClickListener()
    }

    private fun setupViewModel() {
        viewModelFactory = SharedViewModelFactory(requireActivity().application,
            WeatherRepositoryImpl.getInstance(FavLocationLocalDataSourceImpl.getInstance(requireContext())))
        viewModel = ViewModelProvider(this, viewModelFactory).get(SharedViewModel::class.java)
    }

    private fun setupRecyclerView() {
        val favAdapter = FavoritesAdapter(requireContext()) {
            // delete from fav
        }
        viewModel.favList.observe(viewLifecycleOwner) { favList ->
            favAdapter.submitList(favList)
            binding.groupNoFav.visibility = if (favList.isEmpty()) View.VISIBLE else View.GONE
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = favAdapter
        }
    }

    private fun setupFabClickListener() {
        binding.fabAddFav.setOnClickListener {
            val action = FavoritesFragmentDirections.actionFavoritesFragmentToMapFragment(true)
            findNavController().navigate(action)
        }
    }
}
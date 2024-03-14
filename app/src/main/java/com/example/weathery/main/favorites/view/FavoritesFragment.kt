package com.example.weathery.main.favorites.view

import android.graphics.Canvas
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weathery.R
import com.example.weathery.data.database.FavLocationLocalDataSourceImpl
import com.example.weathery.data.network.WeatherRemoteDataSourceImpl
import com.example.weathery.databinding.FragmentFavoritesBinding
import com.example.weathery.main.shared.WeatherViewModel
import com.example.weathery.main.shared.WeatherViewModelFactory
import com.example.weathery.data.repositories.WeatherRepositoryImpl
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


class FavoritesFragment : Fragment() {

    lateinit var binding: FragmentFavoritesBinding
    lateinit var viewModelFactory: WeatherViewModelFactory
    lateinit var viewModel: WeatherViewModel
    lateinit var favAdapter: FavoritesAdapter

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
        viewModelFactory = WeatherViewModelFactory(requireActivity().application,
            WeatherRepositoryImpl.getInstance(FavLocationLocalDataSourceImpl.getInstance(requireContext()),
                WeatherRemoteDataSourceImpl))
        viewModel = ViewModelProvider(this, viewModelFactory).get(WeatherViewModel::class.java)
    }


    private fun setupRecyclerView() {
        favAdapter = FavoritesAdapter(requireContext(),
            { viewModel.delFromFav(it) }) {
            val location = Location("fav")
            location.longitude = it.forecast?.lon as Double
            location.latitude = it.forecast?.lat as Double
            val action = FavoritesFragmentDirections.actionFavoritesFragmentToWeatherFragment(location)
            findNavController().navigate(action)
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val favorite = favAdapter.currentList[position]
                viewModel.delFromFav(favorite)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean,
            ) {
                RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
                    .addActionIcon(R.drawable.ic_del)
                    .create()
                    .decorate()
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)


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
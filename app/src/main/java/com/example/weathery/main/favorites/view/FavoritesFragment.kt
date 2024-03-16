package com.example.weathery.main.favorites.view

import android.content.DialogInterface
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
import com.example.weathery.data.models.FavLocationsWeather
import com.example.weathery.data.network.WeatherRemoteDataSourceImpl
import com.example.weathery.databinding.FragmentFavoritesBinding
import com.example.weathery.main.shared.WeatherViewModel
import com.example.weathery.main.shared.WeatherViewModelFactory
import com.example.weathery.data.repositories.WeatherRepositoryImpl
import com.example.weathery.utils.CustomAlertDialog
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


class FavoritesFragment : Fragment() {

    lateinit var binding: FragmentFavoritesBinding
    lateinit var viewModelFactory: WeatherViewModelFactory
    lateinit var viewModel: WeatherViewModel
    lateinit var favAdapter: FavoritesAdapter
    lateinit var alertDialog: CustomAlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        alertDialog = CustomAlertDialog(requireActivity())
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
            { showDeleteMessage(it) })
        {navigateToFavDetails(it)}

        val itemTouchHelper = ItemTouchHelper(getItemTouchHelper())
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

    private fun getItemTouchHelper() : ItemTouchHelper.SimpleCallback{
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
        return itemTouchHelperCallback
    }
    private fun navigateToFavDetails(fav: FavLocationsWeather){
        val location = Location("fav")
        location.longitude = fav.forecast?.lon as Double
        location.latitude = fav.forecast.lat as Double
        viewModel.showFav = true
        val action = FavoritesFragmentDirections.actionFavoritesFragmentToFavoritesDailtailsFragment(location)
        findNavController().navigate(action)
    }

    private fun showDeleteMessage(fav: FavLocationsWeather){
        alertDialog.showCustomDialog(
            getString(R.string.delete_item),
            getString(R.string.sure_delete),
            getString(R.string.delete),
            getString(R.string.cancel),
            { dialog, which -> viewModel.delFromFav(fav)},
            { dialog, which -> },
            { })
    }

    private fun setupFabClickListener() {
        binding.fabAddFav.setOnClickListener {
            val action = FavoritesFragmentDirections.actionFavoritesFragmentToMapFragment(true)
            findNavController().navigate(action)
        }
    }
}
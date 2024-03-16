package com.example.weathery.main.alert.view

import android.graphics.Canvas
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weathery.R
import com.example.weathery.data.database.AlertsLocalDataSourceImpl
import com.example.weathery.data.database.FavLocationLocalDataSourceImpl
import com.example.weathery.data.network.WeatherRemoteDataSourceImpl
import com.example.weathery.data.repositories.AlertsRepository
import com.example.weathery.data.repositories.AlertsRepositoryImpl
import com.example.weathery.data.repositories.WeatherRepositoryImpl
import com.example.weathery.databinding.FragmentAlertsBinding
import com.example.weathery.main.alert.viewModel.AlertsViewModel
import com.example.weathery.main.alert.viewModel.AlertsViewModelFactory
import com.example.weathery.main.favorites.view.FavoritesAdapter
import com.example.weathery.main.shared.WeatherViewModel
import com.example.weathery.main.shared.WeatherViewModelFactory
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


class AlertsFragment : Fragment() {

    lateinit var binding: FragmentAlertsBinding
    lateinit var alertViewModelFactory: AlertsViewModelFactory
    lateinit var alertsViewModel: AlertsViewModel
    lateinit var weatherViewModelFactory: WeatherViewModelFactory
    lateinit var weatherViewModel: WeatherViewModel
    lateinit var alertsAdapter: AlertsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModels()
        setupRecyclerView()
        setupFabClickListener()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAlertsBinding.inflate(inflater, container, false)
        return binding.root
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
                val alert = alertsAdapter.currentList[position]
                alertsViewModel.deleteAlert(alert)
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

    private fun setupRecyclerView(){
        alertsAdapter = AlertsAdapter(requireContext()){}
        val itemTouchHelper = ItemTouchHelper(getItemTouchHelper())
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewAlarms)
        alertsViewModel.alertsList.observe(viewLifecycleOwner) { list ->
            alertsAdapter.submitList(list)
            binding.groupNoAlarm.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        }
        binding.recyclerViewAlarms.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = alertsAdapter
        }
    }
    private fun setupFabClickListener(){
        binding.fabAddAlarm.setOnClickListener {

        }
    }

    private fun setupViewModels() {
        alertViewModelFactory = AlertsViewModelFactory(AlertsRepositoryImpl.getInstance(
                AlertsLocalDataSourceImpl.getInstance(requireContext())))
        alertsViewModel = ViewModelProvider(this, alertViewModelFactory).get(AlertsViewModel::class.java)

        weatherViewModelFactory = WeatherViewModelFactory(requireActivity().application,
            WeatherRepositoryImpl.getInstance(
                FavLocationLocalDataSourceImpl.getInstance(requireContext()),
                WeatherRemoteDataSourceImpl
            ))
        weatherViewModel = ViewModelProvider(this, weatherViewModelFactory).get(WeatherViewModel::class.java)
    }


}
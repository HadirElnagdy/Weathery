package com.example.weathery.main.favorites.view

import android.content.Context
import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weathery.R
import com.example.weathery.databinding.FavCellBinding
import com.example.weathery.models.FavLocationsWeather
import com.example.weathery.utils.SimpleUtils
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


class FavoritesAdapter(
    private var context: Context,
    private var onSwipeDelete: (FavLocationsWeather) -> Unit,
):
    ListAdapter<FavLocationsWeather, FavViewHolder>(WeatherDiffUtil()){

    lateinit var binding: FavCellBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = FavCellBinding.inflate(inflater, parent, false)
        return FavViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.binding.txtFavCity.text = currentItem.locality
        holder.binding.txtFavTemp.text = currentItem.forecast?.current?.temp.toString()
        holder.binding.txtFavWeather.text = currentItem.forecast?.current?.weather?.get(0)?.main
        holder.binding.imgFav.setImageResource(SimpleUtils.getIconResourceId(currentItem.forecast?.current?.weather?.get(0)?.icon?:""))
    }
    /*override fun onItemDismiss(position: Int) {
        val currentItem = currentList[position]
        onSwipeDelete(currentItem)
    }*/

}

class FavViewHolder(var binding: FavCellBinding):RecyclerView.ViewHolder(binding.root)
class WeatherDiffUtil: DiffUtil.ItemCallback<FavLocationsWeather>() {
    override fun areItemsTheSame(oldItem: FavLocationsWeather, newItem: FavLocationsWeather): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: FavLocationsWeather, newItem: FavLocationsWeather): Boolean {
        return oldItem == newItem
    }
}
abstract class SwipeGesture(var context: Context): ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ): Boolean {
        return false
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
            .addBackgroundColor(ContextCompat.getColor(context, R.color.red))
            .addActionIcon(R.drawable.ic_del)
            .create()
            .decorate()
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

}
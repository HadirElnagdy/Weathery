package com.example.weathery.main.weather.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weathery.databinding.HourlyCellBinding
import com.example.weathery.models.HourlyItem
import com.example.weathery.utils.SimpleUtils
import java.text.SimpleDateFormat
import java.util.*

class HourlyRecyclerViewAdapter(var context: Context):
    ListAdapter<HourlyItem, HourlyViewHolder>(MyDiffUtil()){

    lateinit var binding: HourlyCellBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = HourlyCellBinding.inflate(inflater, parent, false)
        return HourlyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        val currentHour = getItem(position)
        currentHour.dt?.let {
            val date = Date(it.toLong()*1000)
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.time = date
            val sdf = SimpleDateFormat("hh a")
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val formattedHour = sdf.format(date)
            holder.binding.txtHour.text = formattedHour.toString()

        }
        holder.binding.txtCellTemp.text = currentHour.temp.toString()
        holder.binding.imgCellIcon.setImageResource(
            SimpleUtils
            .getIconResourceId(currentHour?.weather?.get(0)?.icon?:""))

    }

}

class HourlyViewHolder(var binding: HourlyCellBinding): RecyclerView.ViewHolder(binding.root)

class MyDiffUtil():  DiffUtil.ItemCallback<HourlyItem>() {
    override fun areItemsTheSame(oldItem: HourlyItem, newItem: HourlyItem): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: HourlyItem, newItem: HourlyItem): Boolean {
        return oldItem == newItem
    }

}
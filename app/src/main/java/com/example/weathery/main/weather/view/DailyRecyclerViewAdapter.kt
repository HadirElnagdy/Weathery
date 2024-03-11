package com.example.weathery.main.weather.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getString
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weathery.R
import com.example.weathery.databinding.DailyCellBinding
import com.example.weathery.models.DailyItem
import com.example.weathery.utils.SimpleUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.TimeZone
import kotlin.math.log

class DailyRecyclerViewAdapter(var context: Context, var onClick: (DailyItem) -> Unit):
    ListAdapter<DailyItem, DailyViewHolder>(DailyDiffUtil()){
    private val TAG = "DailyRecyclerViewAdapter"
    lateinit var binding: DailyCellBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DailyCellBinding.inflate(inflater, parent, false)
        return DailyViewHolder(binding)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        val currentItem = currentList.get(position)
        holder.binding.imgDailyIcon.setImageResource(SimpleUtils
            .getIconResourceId(currentItem?.weather?.get(0)?.icon?:""))
        currentItem.dt?.let {
            val date = Date(it.toLong() * 1000)
            val sdf = SimpleDateFormat("EEE"/*, Locale("en")*/)
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val formattedDay = sdf.format(date)
            holder.binding.txtDay.text = if(position == 0) getString(context, R.string.today) else formattedDay
        }
        holder.binding.txtLow.text = "${getString(context, R.string.low)} ${currentItem.temp?.min.toString()}\u00B0"
        holder.binding.txtHigh.text = "${getString(context, R.string.high)} ${currentItem.temp?.max.toString()}\u00B0"
        Log.i(TAG, "onBindViewHolder: ${currentItem.temp.toString()}\\u00B0")
        holder.binding.root.setOnClickListener {
            onClick(currentItem)
        }
    }

}

class DailyViewHolder(var binding: DailyCellBinding): RecyclerView.ViewHolder(binding.root)

class DailyDiffUtil():  DiffUtil.ItemCallback<DailyItem>() {
    override fun areItemsTheSame(oldItem: DailyItem, newItem: DailyItem): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: DailyItem, newItem: DailyItem): Boolean {
        return oldItem == newItem
    }


}
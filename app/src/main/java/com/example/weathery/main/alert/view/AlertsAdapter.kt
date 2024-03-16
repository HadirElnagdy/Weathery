package com.example.weathery.main.alert.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weathery.data.models.Alert
import com.example.weathery.databinding.AlertCellBinding


class AlertsAdapter(
    private var context: Context,
    private var onSwipeDelete: (Alert) -> Unit,
):
    ListAdapter<Alert, AlertViewHolder>(AlertDiffUtil()), ItemTouchHelperAdapter{

    lateinit var binding: AlertCellBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = AlertCellBinding.inflate(inflater, parent, false)
        return AlertViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        val currentItem = getItem(position)
//        holder.binding.imgAlertType.setImageResource(R.drawable.)
        holder.binding.txtAlertTime.text = currentItem.endTime
        holder.binding.txtAlertType.text = currentItem.alertType

    }


    override fun onItemDismiss(position: Int) {
        val currentItem = currentList[position]
        onSwipeDelete(currentItem)
    }

}

class AlertViewHolder(var binding: AlertCellBinding):RecyclerView.ViewHolder(binding.root)
class AlertDiffUtil: DiffUtil.ItemCallback<Alert>() {
    override fun areItemsTheSame(oldItem: Alert, newItem: Alert): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Alert, newItem: Alert): Boolean {
        return oldItem == newItem
    }
}
interface ItemTouchHelperAdapter {
    fun onItemDismiss(position: Int)
}
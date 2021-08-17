package com.example.kotlinstart.view.weatherhistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinstart.databinding.ItemWeatherHistoryBinding
import com.example.kotlinstart.room.HistoryEntity
import com.example.kotlinstart.view.weatherhistory.WeatherHistoryAdapter.WeatherHistoryHolder

class WeatherHistoryAdapter(private var historyEntity: List<HistoryEntity>) :
    RecyclerView.Adapter<WeatherHistoryHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherHistoryHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemWeatherHistoryBinding =
            ItemWeatherHistoryBinding.inflate(layoutInflater, parent, false)
        return WeatherHistoryHolder(itemWeatherHistoryBinding)
    }

    override fun onBindViewHolder(holder: WeatherHistoryHolder, position: Int) {
        holder.bind(historyEntity[position])
    }

    override fun getItemCount(): Int {
        return historyEntity.size
    }

    inner class WeatherHistoryHolder(private val itemWeatherHistoryBinding: ItemWeatherHistoryBinding) :
        RecyclerView.ViewHolder(itemWeatherHistoryBinding.root) {
        fun bind(historyEntity: HistoryEntity) {
            itemWeatherHistoryBinding.historyId.text = historyEntity.id.toString()
            itemWeatherHistoryBinding.textViewCity.text = historyEntity.city
            itemWeatherHistoryBinding.temperature.text = historyEntity.temperature
            itemWeatherHistoryBinding.condition.text = historyEntity.condition
        }
    }
}
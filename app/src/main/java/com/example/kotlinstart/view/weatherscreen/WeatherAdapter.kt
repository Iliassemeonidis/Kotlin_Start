package com.example.kotlinstart.view.weatherscreen

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinstart.databinding.ItemCityWeatherBinding
import com.example.kotlinstart.model.Weather

class WeatherAdapter(
    private var weatherList: ArrayList<Weather>,
    private val onClickItem: WeatherFragment.OnClickItem
) : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>(), ItemTouchHelperAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemPersonBinding = ItemCityWeatherBinding.inflate(layoutInflater, parent, false)
        return WeatherViewHolder(itemPersonBinding)
    }

    override fun getItemCount() = weatherList.size

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(weatherList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItemInList(weather: Weather) {
        weatherList.add(weather)
        notifyItemInserted(weatherList.size - 1)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        weatherList.removeAt(fromPosition).apply {
            weatherList.add(if (toPosition > fromPosition) toPosition - 1 else toPosition, this)
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        weatherList.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class WeatherViewHolder(private val itemWeatherBinding: ItemCityWeatherBinding) :
        RecyclerView.ViewHolder(itemWeatherBinding.root), ItemTouchHelperViewHolder {
        fun bind(weather: Weather) {
            itemWeatherBinding.textViewCity.text = weather.cityName
            itemWeatherBinding.textViewRegion.text = weather.region
            itemWeatherBinding.temperature.text = weather.temperature
            itemView.setOnClickListener { onClickItem.onClick(weatherList[adapterPosition]) }
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.GRAY)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(0)
        }
    }
}

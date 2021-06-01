package com.example.kotlinstart.view.weatherscreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinstart.databinding.ItemCityWeatherBinding
import com.example.kotlinstart.view.data.Weather

internal class WeatherAdapter(
    @Nullable
    private var weatherList: ArrayList<Weather>,
    private val onClickItem: WeatherFragment.OnClickItem
) : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemPersonBinding = ItemCityWeatherBinding.inflate(layoutInflater, parent, false)
        return WeatherViewHolder(itemPersonBinding)
    }

    override fun getItemCount() = weatherList.size

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(weatherList[position])
    }

    inner class WeatherViewHolder(private val itemWeatherBinding: ItemCityWeatherBinding) :
        RecyclerView.ViewHolder(itemWeatherBinding.root) {
        fun bind(weather: Weather) {
            itemWeatherBinding.textViewCity.text = weather.cityName
            itemWeatherBinding.textViewRegion.text = weather.region
            itemWeatherBinding.temperature.text = weather.temperature
            itemView.setOnClickListener { onClickItem.onClick(weatherList[adapterPosition]) }
        }
    }
}

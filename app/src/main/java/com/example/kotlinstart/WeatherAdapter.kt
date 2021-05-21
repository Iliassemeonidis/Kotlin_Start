package com.example.kotlinstart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

internal class WeatherAdapter(
    var weatherList: ArrayList<Weather>,
) : RecyclerView.Adapter<WeatherAdapter.MainFragmentWeatherList>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainFragmentWeatherList =
        MainFragmentWeatherList(
            LayoutInflater.from(parent.context).inflate(R.layout.item_city_weather, parent, false)
        )

    override fun getItemCount() = weatherList.size

    override fun onBindViewHolder(holder: MainFragmentWeatherList, position: Int) {
        holder.bind(weatherList[position])
    }

    class MainFragmentWeatherList(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var city: TextView = itemView.findViewById(R.id.text_view_city)
        private var region: TextView = itemView.findViewById(R.id.text_view_region)
        private var temp: TextView = itemView.findViewById(R.id.temperature)

        fun bind(weather: Weather) {
            city.text = weather.cityName
            temp.text = weather.temperature
            region.text = weather.region
        }
    }
}
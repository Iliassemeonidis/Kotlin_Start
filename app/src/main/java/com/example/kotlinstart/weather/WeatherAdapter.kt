package com.example.kotlinstart.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinstart.R

internal class WeatherAdapter(
    private var weatherList: ArrayList<Weather>,
    private val onClickItem: WeatherFragment.OnClickItem
) : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder =
        WeatherViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_city_weather, parent, false)
        )

    override fun getItemCount() = weatherList.size

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(weatherList[position])

    }

   inner class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private var city: TextView = itemView.findViewById(R.id.text_view_city)
        private var region: TextView = itemView.findViewById(R.id.text_view_region)
        private var temp: TextView = itemView.findViewById(R.id.temperature)
        init {
            itemView.setOnClickListener{ onClickItem.onClick(weatherList[adapterPosition])}
        }

        fun bind(weather: Weather) {
            city.text = weather.cityName
            temp.text = weather.temperature
            region.text = weather.region
        }


    }
}
package com.example.kotlinstart.view.weatherlistscreen

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinstart.databinding.ItemCityWeatherBinding
import com.example.kotlinstart.model.Weather
import com.example.kotlinstart.repository.weatherrepository.RepositoryImpl

internal class WeatherListAdapter(
    private var onClickItem: WeatherListFragment.OnClickItem?,
    private val onItemDeleteListener: OnItemDeleteListener
) : RecyclerView.Adapter<WeatherListAdapter.WeatherViewHolder>(), ItemTouchHelperAdapter {

    private var weatherList: MutableList<Weather> = mutableListOf()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemPersonBinding = ItemCityWeatherBinding.inflate(layoutInflater, parent, false)
        return WeatherViewHolder(itemPersonBinding)
    }

    override fun getItemCount() = weatherList.size

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(weatherList[position])
    }

    fun onItemAdded(weather: Weather) {
        weatherList.add(weather)
        if (weatherList.size > 1) {
            notifyItemInserted(weatherList.size - 1)
        } else {
            notifyItemInserted(0)
        }
    }

    fun hasWeatherInList(cityName: String): Boolean {
        var result = false
        weatherList.map {
            if (it.cityName == cityName) {
                result = true
            }
        }
        return result
    }


    @SuppressLint("NotifyDataSetChanged")
    fun onListAdded(list: MutableList<Weather>) {
        weatherList = list
        notifyDataSetChanged()
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        weatherList.removeAt(fromPosition).apply {
            weatherList.add(if (toPosition > fromPosition) toPosition - 1 else toPosition, this)
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        onItemDeleteListener.onItemDelete(true,weatherList[position])
        weatherList.removeAt(position)
        notifyItemRemoved(position)
    }


    fun onDestroy() {
        onClickItem = null
    }

    inner class WeatherViewHolder(private val itemWeatherBinding: ItemCityWeatherBinding) :
        RecyclerView.ViewHolder(itemWeatherBinding.root), ItemTouchHelperViewHolder {
        fun bind(weather: Weather) {
            itemWeatherBinding.textViewCity.text = weather.cityName
            itemWeatherBinding.textViewRegion.text = weather.region
            itemWeatherBinding.temperature.text = weather.temperature
            itemView.setOnClickListener {
                onClickItem?.onClick(adapterPosition)
            }
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.GREEN)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(Color.RED)
        }
    }

}

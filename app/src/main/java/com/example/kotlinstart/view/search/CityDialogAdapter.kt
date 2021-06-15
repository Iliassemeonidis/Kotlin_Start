package com.example.kotlinstart.view.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinstart.databinding.ItemCityBinding
import com.example.kotlinstart.view.data.CityData

internal class CityDialogAdapter(private val cityList: ArrayList<CityData>) :
    RecyclerView.Adapter<CityDialogAdapter.CityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemCityBinding = ItemCityBinding.inflate(layoutInflater, parent, false)
        return CityViewHolder(itemCityBinding)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.bind(cityList[position])
    }

    override fun getItemCount(): Int = cityList.size

    inner class CityViewHolder(private val itemCityBinding: ItemCityBinding) :
        RecyclerView.ViewHolder(itemCityBinding.root) {
        fun bind(cityName: CityData) {
            itemCityBinding.textViewCityName.text = cityName.cityName
        }
    }
}

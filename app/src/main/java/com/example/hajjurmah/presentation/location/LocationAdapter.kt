package com.example.hajjurmah.presentation.location

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hajjurmah.domain.OnItemClickLocationListener
import com.example.hajjurmah.data.LocationResponse
import kz.hack.coumrah.databinding.ItemLocationBinding

class LocationAdapter : RecyclerView.Adapter<LocationAdapter.PlacesViewHolder>() {

    private val placesList = mutableListOf<LocationResponse>()
    private var itemClickListener: OnItemClickLocationListener? = null


    inner class PlacesViewHolder(private val binding: ItemLocationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(placeResponse: LocationResponse) {
            binding.name.text = placeResponse.title
            val adapterPhoto = PhotoAdapter()
            binding.rcImage.adapter = adapterPhoto
            binding.description.text = placeResponse.description
            adapterPhoto.submitList(placeResponse.photo)
            binding.address.text = placeResponse.address
            binding.booking.text = placeResponse.onlineBooking
            binding.btnLocation.setOnClickListener {
                itemClickListener!!.onClickToLocation(placeResponse.latitude,placeResponse.longitude)
            }
        }
    }
    fun setOnItemClickListener(listener: OnItemClickLocationListener) {
        this.itemClickListener = listener
    }

    fun submitList(list:List< LocationResponse>) {
        placesList.clear()
        placesList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlacesViewHolder {
        return PlacesViewHolder(
            ItemLocationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PlacesViewHolder, position: Int) {
        holder.onBind(placesList[position])
    }

    override fun getItemCount(): Int {
        return placesList.size
    }
}
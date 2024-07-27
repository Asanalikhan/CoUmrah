package com.example.hajjurmah.location

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hajjurmah.OnItemClickListener
import com.example.hajjurmah.data.LocationResponse
import kz.hack.coumrah.databinding.ItemLocationBinding
import kz.hack.coumrah.databinding.ItemPhotoBinding

class PhotoAdapter  : RecyclerView.Adapter<PhotoAdapter.PlacesViewHolder>() {

    private val placesList = mutableListOf<String>()

    inner class PlacesViewHolder(private val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(photoUrl:String) {
            Glide.with(binding.root).load(photoUrl).into(binding.imgPhotoPlaces)
        }
    }

    fun submitList(list:List<String>) {
        placesList.clear()
        placesList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlacesViewHolder {
        return PlacesViewHolder(
            ItemPhotoBinding.inflate(
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
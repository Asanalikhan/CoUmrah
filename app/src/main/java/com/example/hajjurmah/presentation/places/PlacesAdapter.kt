package com.example.hajjurmah.presentation.places

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hajjurmah.domain.OnItemClickListener
import com.example.hajjurmah.data.PlaceModel
import kz.hack.coumrah.databinding.ItemPlacesBinding

class PlacesAdapter : RecyclerView.Adapter<PlacesAdapter.PlacesViewHolder>() {

    private val placesList = mutableListOf<PlaceModel>()
    private var itemClickListener: OnItemClickListener? = null

    inner class PlacesViewHolder(private val binding: ItemPlacesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(placeModel: PlaceModel) {
            binding.textView.text = placeModel.placeObject.name
            Glide.with(binding.root).load(placeModel.placeObject.backgroundPhoto).into(binding.imageView)
            binding.root.setOnClickListener{
                itemClickListener!!.onItemClick(placeModel.id)
            }
        }
    }
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list:List< PlaceModel>) {
        placesList.clear()
        placesList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlacesViewHolder {
        return PlacesViewHolder(
            ItemPlacesBinding.inflate(
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
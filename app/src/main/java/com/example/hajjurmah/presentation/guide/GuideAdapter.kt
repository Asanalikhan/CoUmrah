package com.example.hajjurmah.presentation.guide

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hajjurmah.domain.OnItemClickLocationListener
import com.example.hajjurmah.data.GuideResponse
import com.example.hajjurmah.domain.OnItemClickCall
import com.example.hajjurmah.presentation.location.PhotoAdapter
import kz.hack.coumrah.databinding.ItemGuideBinding

class GuideAdapter() : RecyclerView.Adapter<GuideAdapter.GuideViewHolder>() {

    private val guideList = mutableListOf<GuideResponse>()
    private var itemClickListener: OnItemClickCall? = null
    private val adapterPhoto = PhotoAdapter()

    inner class GuideViewHolder(private val binding: ItemGuideBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(guideResponse: GuideResponse) {
            binding.cost.text = guideResponse.cost
            binding.data.text = "${guideResponse.data}, ${guideResponse.time}"
            binding.available.text = guideResponse.availablePlace
            binding.photoToursRecyclerView.adapter = adapterPhoto
            adapterPhoto.submitList(guideResponse.photoTours)
            binding.nameGuide.text = guideResponse.title
            binding.descriptionGuide.text = guideResponse.description
            Glide.with(binding.root.context).load(guideResponse.photoGuide).into(binding.photoGuide)
            binding.title.text = guideResponse.nameGuide
            binding.positionGuide.text = guideResponse.positionGuide
            binding.description.text = guideResponse.descriptionGuide
            binding.btnCall.setOnClickListener {
                itemClickListener?.onItemClick(guideResponse.phone)
            }
        }
    }
    fun setOnItemClickListener(listener: OnItemClickCall) {
        this.itemClickListener = listener
    }

    fun submitList(list:List<GuideResponse>) {
        guideList.clear()
        guideList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GuideViewHolder {
        return GuideViewHolder(
            ItemGuideBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: GuideViewHolder, position: Int) {
        holder.onBind(guideList[position])
    }

    override fun getItemCount(): Int {
        return guideList.size
    }
}
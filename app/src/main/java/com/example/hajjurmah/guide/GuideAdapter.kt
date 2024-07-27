package com.example.hajjurmah.guide

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hajjurmah.OnItemClickLocationListener
import com.example.hajjurmah.data.GuideResponse
import com.example.hajjurmah.location.PhotoAdapter
import kz.hack.coumrah.databinding.ItemGuideBinding

class GuideAdapter() : RecyclerView.Adapter<GuideAdapter.GuideViewHolder>() {

    private val guideList = mutableListOf<GuideResponse>()
    private var itemClickListener: OnItemClickLocationListener? = null
    private val adapterPhoto = PhotoAdapter()

    inner class GuideViewHolder(private val binding: ItemGuideBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(guideResponse: GuideResponse) {
            binding.nameGuide.text = guideResponse.nameGuide
            binding.positionGuide.text = guideResponse.positionGuide
            binding.title.text = guideResponse.title
            binding.data.text = guideResponse.data
            binding.description.text = guideResponse.description
            Glide.with(binding.root).load(binding.photoGuide).into(binding.photoGuide)
            binding.time.text = guideResponse.time
            binding.descriptionGuide.text = guideResponse.descriptionGuide

            adapterPhoto.submitList(guideResponse.photoTours)
            binding.root.setOnClickListener {

            }
        }
    }
    fun setOnItemClickListener(listener: OnItemClickLocationListener) {
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
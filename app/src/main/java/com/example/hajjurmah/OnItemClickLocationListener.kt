package com.example.hajjurmah

import com.google.gson.annotations.SerializedName

interface OnItemClickLocationListener {
    fun onClickToLocation(
        latitude: String,
        longitude: String
    )
}
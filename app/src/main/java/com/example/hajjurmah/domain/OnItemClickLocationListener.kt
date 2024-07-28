package com.example.hajjurmah.domain

interface OnItemClickLocationListener {
    fun onClickToLocation(
        latitude: String,
        longitude: String
    )
}
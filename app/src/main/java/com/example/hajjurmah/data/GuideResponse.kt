package com.example.hajjurmah.data

import com.google.gson.annotations.SerializedName

data class GuideResponse(
    @SerializedName("availablePlace")
    val availablePlace: String,
    @SerializedName("cost")
    val cost: String,
    @SerializedName("data")
    val data: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("descriptionGuide")
    val descriptionGuide: String,
    @SerializedName("nameGuide")
    val nameGuide: String,
    @SerializedName("photoGuide")
    val photoGuide: String,
    @SerializedName("photoTours")
    val photoTours: List<String>,
    @SerializedName("positionGuide")
    val positionGuide: String,
    @SerializedName("time")
    val time: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("phone")
    val phone: String,
)
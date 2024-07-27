package com.example.hajjurmah.data

import com.google.gson.annotations.SerializedName

data class GuideResponse(

    @SerializedName("description")
    val description: String, // The Clock Towers, is a government-owned complex of seven skyscraper hotels in Mecca, Saudi Arabia. These towers are a part of the King Abdulaziz Endowment Project that aims to modernize the city in catering to its pilgrims. The central hotel tower, which is the Makkah Clock Royal Tower, is the fourth-tallest building and sixth-tallest freestanding structure in the world. The clock tower contains the Clock Tower Museum that occupies the top four floors of the tower.
    @SerializedName("photoTours")
    val photoTours: List<String>,
    @SerializedName("title")
    val title: String, // Clock Tower
    @SerializedName("time")
    val time: String, // Time
    @SerializedName("positionGuide")
    val positionGuide: String, // Clock Tower
    @SerializedName("photoGuide")
    val photoGuide: String,
    @SerializedName("nameGuide")
    val nameGuide: String,
    @SerializedName("descriptionGuide")
    val descriptionGuide: String,
    @SerializedName("data")
    val data: String
)
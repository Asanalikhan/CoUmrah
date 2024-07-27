package com.example.hajjurmah.data


import com.google.gson.annotations.SerializedName

data class LocationResponse(
    @SerializedName("address")
    val address: String, // Mecca Mecca 24529
    @SerializedName("description")
    val description: String, // The Clock Towers, is a government-owned complex of seven skyscraper hotels in Mecca, Saudi Arabia. These towers are a part of the King Abdulaziz Endowment Project that aims to modernize the city in catering to its pilgrims. The central hotel tower, which is the Makkah Clock Royal Tower, is the fourth-tallest building and sixth-tallest freestanding structure in the world. The clock tower contains the Clock Tower Museum that occupies the top four floors of the tower.
    @SerializedName("Latitude")
    val latitude: String, // 21°25′8″N
    @SerializedName("Longitude")
    val longitude: String, // 39°49′35″E
    @SerializedName("onlineBooking")
    val onlineBooking: String, // Не имеется
    @SerializedName("photo")
    val photo: List<String>,
    @SerializedName("title")
    val title: String // Clock Tower
)
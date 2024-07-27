package com.example.hajjurmah.data


import com.google.gson.annotations.SerializedName

data class PlaceResponse(
    @SerializedName("backgroundPhoto")
    val backgroundPhoto: String, // https://th.bing.com/th/id/R.f9cf289f835f864dde13c4cef955cb40?rik=YW64TuID0O0iFw&pid=ImgRaw&r=0
    @SerializedName("name")
    val name: String // Medina
)
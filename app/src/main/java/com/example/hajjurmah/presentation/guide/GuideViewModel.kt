package com.example.hajjurmah.presentation.guide

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hajjurmah.data.GuideResponse
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.coroutines.launch


class GuideViewModel(): ViewModel() {
    private val _guides = MutableLiveData<List<GuideResponse>>()
    val guides: LiveData<List<GuideResponse>> = _guides

    private val _errorException = MutableLiveData<String>()
    val errorException: LiveData<String> = _errorException

    fun fetchGuide() {
        viewModelScope.launch {
        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("guides")

        collectionRef.get().addOnSuccessListener { documents ->
            val placesList = mutableListOf<GuideResponse>()
            for (document in documents) {
                val gson = Gson()
                val jsonData = gson.toJson(document.data)
                val guideResponse = gson.fromJson(jsonData, GuideResponse::class.java)
                placesList.add(guideResponse)
            }
            _guides.value = placesList
        }.addOnFailureListener { exception ->
            _errorException.postValue(exception.toString())

        }}
    }
}
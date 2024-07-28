package com.example.hajjurmah.presentation.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hajjurmah.data.LocationResponse
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson

class LocationViewModel : ViewModel() {
    private val _locations = MutableLiveData<List<LocationResponse>>()
    val locations: LiveData<List<LocationResponse>> = _locations

    private val _errorException = MutableLiveData<String>()
    val errorException: LiveData<String> = _errorException

    suspend fun fetchLocations(token:String) {
        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("places")
        val subCollectionRef = collectionRef.document("$token").collection("1")

        subCollectionRef.get().addOnSuccessListener { documents ->
            val locationList = mutableListOf<LocationResponse>()
            for (document in documents) {
                val gson = Gson()
                val jsonData = gson.toJson(document.data)
                val locationResponse = gson.fromJson(jsonData, LocationResponse::class.java)
                locationList.add(locationResponse)
            }
            _locations.value = locationList
        }.addOnFailureListener { exception ->
            _errorException.postValue(exception.toString())
        }
    }
}

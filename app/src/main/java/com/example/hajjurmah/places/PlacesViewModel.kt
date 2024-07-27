package com.example.hajjurmah.places

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hajjurmah.data.PlaceModel
import com.example.hajjurmah.data.PlaceResponse
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson

class PlacesViewModel : ViewModel() {
    private val _places = MutableLiveData<List<PlaceModel>>()
    val places: LiveData<List<PlaceModel>> = _places

    private val _errorException = MutableLiveData<String>()
    val errorException: LiveData<String> = _errorException

    suspend fun fetchPlaces() {
        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("places")

        collectionRef.get().addOnSuccessListener { documents ->
            val placesList = mutableListOf<PlaceModel>()
            for (document in documents) {
                val gson = Gson()
                val jsonData = gson.toJson(document.data)
                document.id
                val placeResponse = gson.fromJson(jsonData, PlaceResponse::class.java)
                val placeModel = PlaceModel(document.id,placeResponse)
                placesList.add(placeModel)
            }
            _places.value = placesList
        }.addOnFailureListener { exception ->
            _errorException.postValue(exception.toString())

        }
    }
}

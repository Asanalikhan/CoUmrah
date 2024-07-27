package com.example.hajjurmah.places

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.hajjurmah.OnItemClickListener
import com.example.hajjurmah.data.PlaceResponse
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.hack.coumrah.R
import kz.hack.coumrah.databinding.FragmentListBinding

class PlacesFragment : Fragment(){

    private lateinit var binding: FragmentListBinding
    private val placesList = mutableListOf<PlaceResponse>()
    private var placesAdapter = PlacesAdapter()

    private val viewModel: PlacesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rcView.adapter = placesAdapter
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.fetchPlaces()
        }
        viewModel.places.observe(viewLifecycleOwner) { it ->
            placesAdapter.submitList(it)
        }
        placesAdapter.setOnItemClickListener(object :OnItemClickListener{
            override fun onItemClick(name: String) {
                val action = PlacesFragmentDirections.actionPlacesFragmentToLocationFragment(name)
                findNavController().navigate(action)
            }

        })
    }

}
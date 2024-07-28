package com.example.hajjurmah.presentation.places

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.hajjurmah.domain.OnItemClickListener
import com.example.hajjurmah.data.PlaceResponse
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.Manifest
import kz.hack.coumrah.R
import kz.hack.coumrah.databinding.BottomnavbarMainBinding
import kz.hack.coumrah.databinding.FragmentListBinding

class PlacesFragment : Fragment(){

    private lateinit var binding: FragmentListBinding
    private val placesList = mutableListOf<PlaceResponse>()
    private var placesAdapter = PlacesAdapter()
    private lateinit var bottomNavBinding: BottomnavbarMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient



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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        binding.rcView.adapter = placesAdapter

        viewModel.fetchPlaces()

        viewModel.places.observe(viewLifecycleOwner) { it ->
            placesAdapter.submitList(it)
        }
        placesAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(name: String) {
                val action = PlacesFragmentDirections.actionPlacesFragmentToLocationFragment(name)
                findNavController().navigate(action)
            }

        })

        bottomNavBinding = BottomnavbarMainBinding.bind(binding.bottomAppBar.getChildAt(0))

        binding.toolbar.btnMenu.setOnClickListener{
            findNavController().navigate(R.id.homeFragment)
        }
        bottomNavBinding.btnGuide.setOnClickListener{
            findNavController().navigate(R.id.guideFragment)
        }

        bottomNavBinding.btnMaps.setOnClickListener {
            getCurrentLocationAndOpenInMaps()
        }

        bottomNavBinding.btnHickmet.setOnClickListener {
            findNavController().navigate(R.id.hickmetFragment)
        }
    }

    private fun getCurrentLocationAndOpenInMaps() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request location permissions
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val latitude = it.latitude
                val longitude = it.longitude
                openLocationInMaps(latitude.toString(), longitude.toString())
            } ?: run {
                Log.e("LocationFragment", "Location is null")
            }
        }.addOnFailureListener { exception ->
            Log.e("LocationFragment", "Failed to get location", exception)
        }
    }

    private fun openLocationInMaps(latitude: String, longitude: String) {
        val gmmIntentUri = Uri.parse("geo:$latitude,$longitude")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

}
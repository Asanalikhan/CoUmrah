package com.example.hajjurmah.presentation.location

import android.Manifest
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
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hajjurmah.domain.OnItemClickLocationListener
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.hack.coumrah.R
import kz.hack.coumrah.databinding.BottomnavbarMainBinding
import kz.hack.coumrah.databinding.FragmentDetailedBinding

class LocationFragment : Fragment() {

    private lateinit var binding: FragmentDetailedBinding
    private val locationViewModel: LocationViewModel by viewModels()
    private val adapterLocation = LocationAdapter()
    private val args : LocationFragmentArgs by  navArgs()
    private lateinit var bottomNavBinding: BottomnavbarMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailedBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rcLocation.adapter = adapterLocation
        adapterLocation.setOnItemClickListener(object : OnItemClickLocationListener {
            override fun onClickToLocation(latitude: String, longitude: String) {
                openLocationInMaps(latitude, longitude)
            }
        })

        lifecycleScope.launch(Dispatchers.IO) {
            Log.d("AAA",args.idName)
            locationViewModel.fetchLocations(args.idName)
        }

        locationViewModel.locations.observe(viewLifecycleOwner, Observer { locations ->
            adapterLocation.submitList(locations)
        })

        bottomNavBinding = BottomnavbarMainBinding.bind(binding.bottomAppBar.getChildAt(0))

        bottomNavBinding.btnMaps.setOnClickListener {
            getCurrentLocationAndOpenInMaps()
        }
        binding.toolbar.btnMenu.setOnClickListener{
            findNavController().navigate(R.id.homeFragment)
        }

        bottomNavBinding.btnHickmet.setOnClickListener {
            findNavController().navigate(R.id.hickmetFragment)
        }
        bottomNavBinding.btnGuide.setOnClickListener{
            findNavController().navigate(R.id.guideFragment)
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
                openCurrentLocationInMaps(latitude.toString(), longitude.toString())
            } ?: run {
                Log.e("LocationFragment", "Location is null")
            }
        }.addOnFailureListener { exception ->
            Log.e("LocationFragment", "Failed to get location", exception)
        }
    }

    private fun openLocationInMaps(latitude: String, longitude: String) {
        val latLong = convertDMS(latitude, longitude)
        Log.d("LocationFragment", "Converted coordinates: $latLong")
        val gmmIntentUri = Uri.parse("geo:$latLong")
        Log.d("LocationFragment", "Geo URI: $gmmIntentUri")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
    }

    private fun convertDMS(latitude: String, longitude: String): String {
        fun dmsToDecimal(degrees: String, minutes: String, seconds: String, direction: String): Double {
            val decimal = degrees.toDouble() + minutes.toDouble() / 60 + seconds.toDouble() / 3600
            return if (direction == "S" || direction == "W") -decimal else decimal
        }

        val latParts = latitude.split("[°′″]".toRegex())
        val lonParts = longitude.split("[°′″]".toRegex())

        val latDirection = if (latitude.contains("N")) "N" else "S"
        val lonDirection = if (longitude.contains("E")) "E" else "W"

        val latDecimal = dmsToDecimal(latParts[0], latParts[1], latParts[2], latDirection)
        val lonDecimal = dmsToDecimal(lonParts[0], lonParts[1], lonParts[2], lonDirection)

        return "$latDecimal,$lonDecimal"
    }

    private fun openCurrentLocationInMaps(latitude: String, longitude: String) {
        val gmmIntentUri = Uri.parse("geo:$latitude,$longitude")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}

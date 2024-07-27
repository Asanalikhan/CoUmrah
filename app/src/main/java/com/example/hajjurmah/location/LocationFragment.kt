package com.example.hajjurmah.location

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hajjurmah.OnItemClickLocationListener
import com.example.hajjurmah.data.LocationResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.hack.coumrah.databinding.FragmentDetailedBinding

class LocationFragment : Fragment() {

    private lateinit var binding: FragmentDetailedBinding
    private val locationViewModel: LocationViewModel by viewModels()
    private val adapterLocation = LocationAdapter()
    private val args : LocationFragmentArgs by  navArgs()

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
        adapterLocation.setOnItemClickListener(object :OnItemClickLocationListener{
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
}

package com.example.hajjurmah.presentation.guide

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
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.hajjurmah.domain.OnItemClickCall
import com.example.hajjurmah.domain.OnItemClickLocationListener
import com.example.hajjurmah.presentation.location.LocationAdapter
import com.example.hajjurmah.presentation.location.LocationViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kz.hack.coumrah.R
import kz.hack.coumrah.databinding.BottomnavbarMainBinding
import kz.hack.coumrah.databinding.FragmentGuideBinding

class GuideFragment : Fragment() {


    private lateinit var binding: FragmentGuideBinding
    private val guideViewModel: GuideViewModel by viewModels()
    private lateinit var bottomNavBinding: BottomnavbarMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGuideBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var adapter = GuideAdapter()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        binding.rcGuide.adapter = adapter

        guideViewModel.fetchGuide()

        guideViewModel.errorException.observe(viewLifecycleOwner) {
            Log.d("AAA", "$it")
        }

        adapter.setOnItemClickListener(object : OnItemClickCall {
            override fun onItemClick(phone: String) {
                dialPhoneNumber(phone)
            }
        })

        guideViewModel.guides.observe(viewLifecycleOwner, Observer { guides ->
            Log.d("AA", guides.toString())
            adapter.submitList(guides)
            adapter.notifyDataSetChanged()
        })
        bottomNavBinding = BottomnavbarMainBinding.bind(binding.bottomAppBar.getChildAt(0))

        bottomNavBinding.btnMaps.setOnClickListener {
            getCurrentLocationAndOpenInMaps()
        }
        bottomNavBinding.btnHickmet.setOnClickListener {
            findNavController().navigate(R.id.hickmetFragment)
        }
        bottomNavBinding.btnGuide.setOnClickListener{
            findNavController().navigate(R.id.guideFragment)
        }
        binding.toolbar.btnMenu.setOnClickListener{
            findNavController().navigate(R.id.homeFragment)
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

    private fun dialPhoneNumber(phone: String) {
        Log.d("GuideFragment", "Dialing phone number: $phone")
        val dialIntent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phone")
        }
        startActivity(dialIntent)
    }

}
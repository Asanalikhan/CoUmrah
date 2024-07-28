package com.example.hajjurmah

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
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kz.hack.coumrah.R
import kz.hack.coumrah.databinding.BottomnavbarMainBinding
import kz.hack.coumrah.databinding.FragmentDetailedBinding
import kz.hack.coumrah.databinding.FragmentHicmetBinding

class HickmetFragment : Fragment() {

    private lateinit var binding: FragmentHicmetBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var bottomNavBinding: BottomnavbarMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHicmetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val webView = binding.tvWebViewHickmet

        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                view?.loadUrl(request?.url.toString())
                return true
            }
        }

        webView.loadUrl("https://hickmet.kz/")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        bottomNavBinding = BottomnavbarMainBinding.bind(binding.bottomAppBar.getChildAt(0))

        bottomNavBinding.btnMaps.setOnClickListener {
            getCurrentLocationAndOpenInMaps()
        }
        bottomNavBinding.btnGuide.setOnClickListener {
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
}
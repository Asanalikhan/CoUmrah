package com.example.hajjurmah.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import kz.hack.coumrah.R
import kz.hack.coumrah.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       binding.apply {
           places.setOnClickListener {
               findNavController().navigate(R.id.action_homeFragment_to_placesFragment)
           }
           guide.setOnClickListener {
               findNavController().navigate(R.id.action_homeFragment_to_guideFragment)
           }
       }

    }
}

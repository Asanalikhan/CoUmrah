package com.example.hajjurmah.guide

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.hajjurmah.location.LocationViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.hack.coumrah.databinding.FragmentGuideBinding

class GuideFragment : Fragment() {


    private lateinit var binding: FragmentGuideBinding
    private var adapter = GuideAdapter()
    private val guideViewModel: GuideViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGuideBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rcGuide.adapter = adapter

        guideViewModel.fetchGuide()

        guideViewModel.errorException.observe(viewLifecycleOwner) {
            Log.d("AAA", "$it")
        }

        guideViewModel.guides.observe(viewLifecycleOwner, Observer { guides ->
            Log.d("AA", guides.toString())
            adapter.submitList(guides)
            adapter.notifyDataSetChanged()
        })

    }

}
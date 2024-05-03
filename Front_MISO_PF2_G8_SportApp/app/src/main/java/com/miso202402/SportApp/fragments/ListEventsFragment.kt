package com.miso202402.SportApp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentAddEventBinding
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentEditEventsBinding

class ListEventsFragment : Fragment() {
    private var _binding: FragmentEditEventsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
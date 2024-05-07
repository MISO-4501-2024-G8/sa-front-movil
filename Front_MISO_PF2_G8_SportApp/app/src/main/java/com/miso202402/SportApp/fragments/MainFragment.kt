package com.miso202402.front_miso_pf2_g8_sportapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.miso202402.front_miso_pf2_g8_sportapp.R
import com.miso202402.front_miso_pf2_g8_sportapp.activities.MainActivity
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentMainBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MainFragment : Fragment() {
    val token : String? = null
    private var _binding: FragmentMainBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = bundleOf(
            "token" to  arguments?.getString("token"),
            "id" to  arguments?.getString("id")
        )
        binding.textViewTrainingSessionFragmentmmain.setOnClickListener{
            findNavController().navigate(R.id.action_FirstFragment_to_AddTrainingPlanFragment, bundle)
        }

        binding.textViewListEventFragmentmmain.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_ListEventsFragment, bundle)
        }
    }

    override fun onResume() {
        super.onResume()
        // Mostrar la toolbar y el fab en este fragmento
        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.showToolbarAndFab()
        mainActivity?.disableBack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
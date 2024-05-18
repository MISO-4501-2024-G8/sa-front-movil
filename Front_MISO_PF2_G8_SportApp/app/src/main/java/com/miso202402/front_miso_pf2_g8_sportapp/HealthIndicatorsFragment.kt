package com.miso202402.front_miso_pf2_g8_sportapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.miso202402.SportApp.src.utils.SharedPreferences
import com.miso202402.front_miso_pf2_g8_sportapp.activities.MainActivity
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentGoalBinding
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentHealthIndicatorsBinding


class HealthIndicatorsFragment : Fragment() {
    private var _binding: FragmentHealthIndicatorsBinding? = null
    private lateinit var preferences: SharedPreferences

    private var domain: String = "http://lb-ms-py-workout-mngr-1790601522.us-east-1.elb.amazonaws.com/"

    private val binding get() = _binding!!
    override fun onAttach(context: Context) {
        super.onAttach(context)
        preferences = SharedPreferences(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHealthIndicatorsBinding.inflate(inflater, container, false)

        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onResume() {
        super.onResume()
        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.showToolbarAndFab()
    }
    private fun showMessageDialog(activity: Context?, message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

}
package com.miso202402.SportApp.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.RequiresExtension
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.miso202402.SportApp.src.models.models.Events
import com.miso202402.SportApp.src.models.models.TrainingPlan
import com.miso202402.SportApp.src.utils.ClickListener
import com.miso202402.SportApp.src.utils.SharedPreferences
import com.miso202402.SportApp.src.utils.WeeksAdapter
import com.miso202402.front_miso_pf2_g8_sportapp.R
import com.miso202402.front_miso_pf2_g8_sportapp.R.*
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentTrainingSessionBinding


class TrainingSessionFragment : Fragment() {


    private var _binding: FragmentTrainingSessionBinding? = null
    private val binding get() = _binding!!
    private var tipoDeporte : String? = null
    private var vectorTipoDeporte  =  arrayOf("Atletismo", "Ciclismo")
    private var user_id: String? = ""
    private lateinit var preferences: SharedPreferences
    private lateinit var trainingPlanList : List<TrainingPlan>
    private var domain: String = "https://g7o4mxf762.execute-api.us-east-1.amazonaws.com/prod/"
    lateinit var listener: ClickListener



    override fun onAttach(context: Context) {
        super.onAttach(context)
        preferences = SharedPreferences(requireContext())
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrainingSessionBinding.inflate(inflater, container, false)
        user_id = preferences.getData<String>("id").toString()
        Log.i("user_id", user_id!!)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var spinner = view.findViewById<Spinner>(R.id.spinner_TrainingSessionFragment)
        activity?.let {
            ArrayAdapter.createFromResource(it,
                                            array.Sport,
                                            android.R.layout.simple_spinner_item
            ).also { arrayAdapter ->
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = arrayAdapter
            }
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                tipoDeporte = vectorTipoDeporte[p2]
                Log.i("mesnaje al selecionar tipo de deporte ", tipoDeporte.toString())
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
        binding.buttonAgregarFragmentTrainingSession.setOnClickListener {
            val bundle = bundleOf(
                "token" to  arguments?.getString("token"),
                "id" to  arguments?.getString("id"),
                "sport" to tipoDeporte
            )
            findNavController().navigate(
                R.id.action_trainingSessionFragment_to_addTrainingPlanFragment,
                bundle)
        }

    }

    private fun CreateTrainingSesion(){

    }



}
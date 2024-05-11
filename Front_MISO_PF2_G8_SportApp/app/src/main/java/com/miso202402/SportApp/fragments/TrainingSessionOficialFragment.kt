package com.miso202402.SportApp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.RadioButton
import com.miso202402.front_miso_pf2_g8_sportapp.R
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentTrainingSessionBinding
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentTrainingSessionOficialBinding


class TrainingSessionOficialFragment : Fragment() {

    private var _binding: FragmentTrainingSessionOficialBinding? = null
    private val binding get() = _binding!!

    var domain : String = "http://lb-ms-py-training-mngr-157212315.us-east-1.elb.amazonaws.com/"
    private var token: String? = ""
    private var id: String? = ""
    private var sport: String? = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrainingSessionOficialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getString("token")?.let {
            this.token = it.toString()
            Log.i("token", this.token.toString())
        }
        arguments?.getString("user_id")?.let {
            this.id = it.toString()
            Log.i("user_id", this.id.toString())
        }

        arguments?.getString("sport")?.let {
            this.sport = it.toString()
            Log.i("sport", this.id.toString())
        }

        var descriptionEditText = view.findViewById<EditText>(R.id.editTexDescription_TrainingSessionOficialFragment)

        var radioButtonVirtual = view.findViewById<RadioButton>(R.id.radioButton_VirtualSession_TrainingSessionOficialFragment)
        var radioButtonPresencial = view.findViewById<RadioButton>(R.id.radioButton_LocalSession_TrainingSessionOficialFragment)
        var radioButtonDeortologo = view.findViewById<RadioButton>(R.id.radioButton_Deportologo_TrainingSessionOficialFragment)
        var radioButtonEntrenmador = view.findViewById<RadioButton>(R.id.radioButton_Entrenador_TrainingSessionOficialFragment)

        var calendarView = view.findViewById<CalendarView>(R.id.calendarView_TrainingSessionOficialFragment)

        var buttonAddPlan = view.findViewById<Button>(R.id.buttonAgregar_TrainingSessionOficialFragment)

    }


}
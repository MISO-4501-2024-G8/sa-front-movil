package com.miso202402.SportApp.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import androidx.core.os.bundleOf
import com.google.android.material.snackbar.Snackbar
import com.miso202402.SportApp.src.models.models.TrainingPlan
import com.miso202402.SportApp.src.utils.SharedPreferences
import com.miso202402.front_miso_pf2_g8_sportapp.R
import com.miso202402.front_miso_pf2_g8_sportapp.activities.MainActivity
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentTrainingPlanAlertsBinding

class TrainingPlanAlertsFragment : Fragment() {
    private var _binding: FragmentTrainingPlanAlertsBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferences: SharedPreferences
    private var domain: String = "http://lb-ms-py-training-mngr-157212315.us-east-1.elb.amazonaws.com/"
    private lateinit var user_id: String;
    private lateinit var tempTrainingPlan: TrainingPlan
    lateinit var btnContinuar: Button
    lateinit var btnAtras: Button
    lateinit var switchStopTraining: Switch
    lateinit var switchNotificationMsg: Switch
    lateinit var switchEmergencyCall: Switch

    private var stop_training: Boolean = false;
    private var notification_msg: Boolean = false;
    private var emergency_call: Boolean = false;
    private var alertasE: String = "1"
    private var typePlan: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        preferences = SharedPreferences(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTrainingPlanAlertsBinding.inflate(inflater, container, false)
        user_id = preferences.getData<String>("id").toString()
        Log.i("user_id", user_id)
        stop_training = arguments?.getBoolean("stop_training") == true
        notification_msg = arguments?.getBoolean("notification_msg") == true
        emergency_call = arguments?.getBoolean("emergency_call") == true
        typePlan = preferences.getData<String>("typePlan").toString()
        alertasE = "1"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                mostrarSnackbar("Utilizar los botones de la aplicacion para navegar.")
                return@setOnKeyListener true
            }
            false
        }
        btnContinuar = binding.buttonContinuarAlertsFragment
        btnAtras = binding.buttonAtrasAlertsFragment
        switchStopTraining = binding.switchStopTraining
        switchNotificationMsg = binding.switchNotificationMsg
        switchEmergencyCall = binding.switchEmergencyCall
        switchStopTraining.isChecked = stop_training
        switchNotificationMsg.isChecked = notification_msg
        switchEmergencyCall.isChecked = emergency_call

        switchStopTraining.setOnCheckedChangeListener{ _, isChecked ->
            if (isChecked) {
                // El switch está encendido
                // Realizar acciones cuando el switch está encendido
                Log.d("Switch", "switchStopTraining 1 encendido")
            } else {
                // El switch está apagado
                // Realizar acciones cuando el switch está apagado
                Log.d("Switch", "switchStopTraining 1 apagado")
            }
            stop_training = isChecked
            alertasE = "1"
        }

        switchNotificationMsg.setOnCheckedChangeListener{ _, isChecked ->
            if (isChecked) {
                // El switch está encendido
                // Realizar acciones cuando el switch está encendido
                Log.d("Switch", "switchNotificationMsg 1 encendido")
            } else {
                // El switch está apagado
                // Realizar acciones cuando el switch está apagado
                Log.d("Switch", "switchNotificationMsg 1 apagado")
            }
            notification_msg = isChecked
            alertasE = "1"
        }

        switchEmergencyCall.setOnCheckedChangeListener{ _, isChecked ->
            if (isChecked) {
                // El switch está encendido
                // Realizar acciones cuando el switch está encendido
                Log.d("Switch", "switchEmergencyCall 1 encendido")
            } else {
                // El switch está apagado
                // Realizar acciones cuando el switch está apagado
                Log.d("Switch", "switchEmergencyCall 1 apagado")
            }
            emergency_call = isChecked
            alertasE = "1"
        }

        btnContinuar.setOnClickListener(){
            val bundle = bundleOf(
                "stop_training" to stop_training,
                "notification_msg" to notification_msg,
                "emergency_call" to emergency_call,
            )
            preferences.saveData("alertasE", alertasE)
            val mainActivity = requireActivity() as? MainActivity
            mainActivity?.navigateToFragment(R.id.addTrainingPlanFragment, "Nuevo Plan", bundle)
        }
        btnAtras.setOnClickListener(){
            val mainActivity = requireActivity() as? MainActivity
            mainActivity?.navigateToFragment(R.id.addTrainingPlanFragment, "Nuevo Plan", null)
        }
    }

    private fun mostrarSnackbar(mensaje: String) {
        view?.let {
            Snackbar.make(it, mensaje, Snackbar.LENGTH_SHORT).show()
        }
    }

}
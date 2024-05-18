package com.miso202402.SportApp.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.miso202402.SportApp.src.models.models.Objective
import com.miso202402.SportApp.src.models.request.TrainingSessionRequest
import com.miso202402.SportApp.src.models.response.TraingSessionResponse
import com.miso202402.SportApp.src.models.response.TrainingPlanResponse
import com.miso202402.SportApp.src.utils.ObjectiveDetailTrainingPlanAdapter
import com.miso202402.SportApp.src.utils.SharedPreferences
import com.miso202402.front_miso_pf2_g8_sportapp.R
import com.miso202402.front_miso_pf2_g8_sportapp.activities.MainActivity
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentInfoTrainingPlanBinding
import com.miso202402.front_miso_pf2_g8_sportapp.src.services.ApiService
import com.miso202402.front_miso_pf2_g8_sportapp.src.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class InfoTrainingPlan : Fragment() {
    private var _binding:FragmentInfoTrainingPlanBinding? = null
    private val binding get() = _binding!!
    private lateinit var listObjectives: List<Objective>
    //private var domain: String = "https://g7o4mxf762.execute-api.us-east-1.amazonaws.com/prod/"
    private var domain : String = "http://lb-ms-py-training-mngr-157212315.us-east-1.elb.amazonaws.com/"
    private var domain_prod: String = "https://g7o4mxf762.execute-api.us-east-1.amazonaws.com/prod/"
    private lateinit var id_training_plan: String;
    private lateinit var user_id: String;
    private var sportType: String = ""
    private var typePlan: String? = ""
    private lateinit var preferences: SharedPreferences

    override fun onAttach(context: Context) {
        super.onAttach(context)
        preferences = SharedPreferences(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInfoTrainingPlanBinding.inflate(inflater, container, false)
        listObjectives = listOf()
        id_training_plan = arguments?.getString("training_plan_id").toString()
        Log.i("id_training_plan", id_training_plan)
        typePlan = preferences.getData<String>("typePlan").toString()
        user_id = preferences.getData<String>("id").toString()
        getTrainingPlanDetailById(id_training_plan)
        binding.recyclerviewListObjectives .setHasFixedSize(true)
        binding.recyclerviewListObjectives.layoutManager = LinearLayoutManager(context)
        binding.recyclerviewListObjectives.adapter = ObjectiveDetailTrainingPlanAdapter(listObjectives)
        binding.buttonAtras.setOnClickListener(){
            val mainActivity = requireActivity() as? MainActivity
            mainActivity?.navigateToFragment(R.id.trainingSessionFragment, "Plan de Entrenamiento")
        }
        binding.buttonAsociar.setOnClickListener(){
            asociarPlanASession(id_training_plan, user_id)
        }
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
    }

    private fun mostrarSnackbar(mensaje: String) {
        view?.let {
            Snackbar.make(it, mensaje, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun getTrainingPlanDetailById(training_plan_id: String){
        val utils = Utils()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val callGetTrainingPlanById = utils.getRetrofit(domain)
                    .create(ApiService::class.java)
                    .getTrainingPlan(training_plan_id)
                    .execute()
                val getTrainingPlan = callGetTrainingPlanById.body() as TrainingPlanResponse?
                Log.i("getTrainingPlan", getTrainingPlan?.code.toString())
                lifecycleScope.launch {
                    if (getTrainingPlan?.code == 200) {
                        var content = getTrainingPlan.training_plan!!
                        binding.namePlan.text = "Nombre: " + content.name
                        binding.descPlan.text = "Descripcion: " + content.description
                        binding.weeksPlan.text = "Semanas: " + content.weeks
                        binding.sportPlan.text = "Deporte: " + content.sport
                        sportType =  content.sport.toString()

                        if(content.typePlan != "basico"){
                            var alerts: String = "Alertas: "
                            binding.riskAlertsPlan.visibility = View.VISIBLE
                            if(content.risk_alerts?.stop_training == 1){
                                alerts += "Parar Entrenamiento,"
                            }
                            if(content.risk_alerts?.notifications == 1){
                                alerts += "Notificaciones,"
                            }
                            if(content.risk_alerts?.enable_phone == 1){
                                alerts += "Habilitar llamadas de emergencia"
                            }
                            binding.riskAlertsPlan.text = alerts
                        }

                        binding.buttonFood.setOnClickListener(){
                            //mostrarSnackbar("En Construccion")
                            val bundle = bundleOf(
                                "training_plan_id" to id_training_plan,
                                "id_food_routine" to content.id_eating_routine
                            )
                            val mainActivity = requireActivity() as? MainActivity
                            mainActivity?.navigateToFragment(R.id.InfoFoodRoutineFragment, "Detalle Rutina Alimentacion",bundle)
                        }

                        binding.buttonRest.setOnClickListener(){
                            //mostrarSnackbar("En Construccion")
                            val bundle = bundleOf(
                                "training_plan_id" to id_training_plan,
                                "id_rest_routine" to content.id_rest_routine
                                )
                            val mainActivity = requireActivity() as? MainActivity
                            mainActivity?.navigateToFragment(R.id.InfoRestRoutineFragment, "Detalle Rutina Descanso",bundle)
                        }

                        listObjectives = content.objectives?.toList() ?: listOf()
                        val filteredObjectives = listObjectives
                            .filter { it.day in listOf("Lunes", "Martes", "Miercoles", "Jueves", "Viernes") } // Filtrar por los días deseados
                            .sortedBy { when(it.day) { // Ordenar los objetivos según el orden de los días
                                "Lunes" -> 1
                                "Martes" -> 2
                                "Miercoles" -> 3
                                "Jueves" -> 4
                                "Viernes" -> 5
                                else -> 6
                            }}
                        binding.recyclerviewListObjectives.setHasFixedSize(true)
                        binding.recyclerviewListObjectives.layoutManager = LinearLayoutManager(context)
                        binding.recyclerviewListObjectives.adapter = ObjectiveDetailTrainingPlanAdapter(filteredObjectives)
                    } else {
                        activity?.let {
                            utils.showMessageDialog(
                                it,
                                "Error Al traer el plan de entrenamiento, intente mas tarde"
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("error",e.message.toString())
            }
        }
    }


    private fun asociarPlanASession(id_training_plan:String, user_id:String){
        val utils = Utils()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val calendar = Calendar.getInstance()
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val event_date = sdf.format(calendar.time)
                Log.i("asociarPlanASession", "create training $id_training_plan $user_id $sportType $event_date")
                val callCreateTrainigSession = utils.getRetrofit(domain_prod)
                    .create(ApiService::class.java)
                    .createTrainigSession(
                        TrainingSessionRequest(
                            user_id,
                            id_training_plan,
                            "plan_training",
                            sportType,
                            event_date
                        )
                    )
                    .execute()
                val createSession = callCreateTrainigSession.body() as TraingSessionResponse?
                Log.i("asociarPlanASession","Sali se la peticion createTrainigSession Rest")
                Log.i("asociarPlanASession","Sali a la peticion createTrainigSession code " + createSession?.code.toString())
                lifecycleScope.launch {
                    if (createSession?.code == 201) {
                        val messageSucces = createSession.message
                        utils.showMessageDialog(context, messageSucces.toString())
                       // findNavController().navigate(R.id.action_EditEventsFragment_to_ListEventsFragment)
                    } else {
                        activity?.let {
                            utils.showMessageDialog(
                                it,
                                "Error No se pudo asocciar correctmente"
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("asociarPlanASession","error: " + e.message.toString())
            }
            Log.i("asociarPlanASession","Fin createTrainigSession Rest")
            withContext(Dispatchers.Main) {
                val mainActivity = requireActivity() as? MainActivity
                mainActivity?.navigateToFragment(R.id.trainingSessionFragment, "Plan de Entrenamiento")
            }
        }
    }



}
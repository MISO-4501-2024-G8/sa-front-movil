package com.miso202402.SportApp.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.miso202402.SportApp.src.models.models.SportSession
import com.miso202402.SportApp.src.models.models.TrainingPlan
import com.miso202402.SportApp.src.models.models.TrainingSession
import com.miso202402.SportApp.src.models.request.SportSessionRequest
import com.miso202402.SportApp.src.models.response.GetAllSportSessionResponse
import com.miso202402.SportApp.src.models.response.GetAllUserTrainingSessionsResponse
import com.miso202402.SportApp.src.models.response.GetSportSessionResponse
import com.miso202402.SportApp.src.utils.SharedPreferences
import com.miso202402.SportApp.src.utils.SportObjectSessionAdapter
import com.miso202402.SportApp.src.utils.SportSessionAdapter
import com.miso202402.SportApp.src.utils.TrainingPlanAdapter
import com.miso202402.SportApp.src.utils.TrainingSessionAdapter
import com.miso202402.front_miso_pf2_g8_sportapp.R
import com.miso202402.front_miso_pf2_g8_sportapp.activities.MainActivity
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentSportSessionRegisterBinding
import com.miso202402.front_miso_pf2_g8_sportapp.src.services.ApiService
import com.miso202402.front_miso_pf2_g8_sportapp.src.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class SportSessionRegister : Fragment() {
    private var _binding: FragmentSportSessionRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferences: SharedPreferences
    private var domain: String = "https://g7o4mxf762.execute-api.us-east-1.amazonaws.com/prod/"
    private var domain_trainning: String =
        "http://lb-ms-py-training-mngr-157212315.us-east-1.elb.amazonaws.com/"
    private lateinit var user_id: String;
    lateinit var typePlan: String
    private lateinit var trainingSessions: List<TrainingSession>
    lateinit var baseTrainingSession: TrainingSession
    var trainingSessionSelected = ""
    var weekSelected = 0
    var daySelected = ""
    var planName = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        preferences = SharedPreferences(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun setData(idTraining:String, weekS: Int, dayS:String){
        trainingSessionSelected = idTraining
        weekSelected = weekS
        daySelected = dayS
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSportSessionRegisterBinding.inflate(inflater, container, false)
        user_id = preferences.getData<String>("id").toString()
        typePlan = preferences.getData<String>("typePlan").toString()
        Log.i("user_id", user_id)
        Log.i("typePlan", typePlan)
        //obtener sesiones de entrenamiento
        fetchTrainingSessions()
        //por cada una sacar la informacion del training plan
        //Traer las sport sessions que ya existen
        //Revisar por cada training session que no este la semana ni el dia (si ya estan quitarlos)
        binding.btnIniciarSesion.setOnClickListener(){
            if(trainingSessionSelected == ""){
                mostrarSnackbar("Debe seleccionar un plan")
                return@setOnClickListener
            }
            else if (weekSelected == 0){
                mostrarSnackbar("Debe seleccionar una semana")
                return@setOnClickListener
            }
            else if(daySelected == ""){
                mostrarSnackbar("Debe seleccionar un dia")
                return@setOnClickListener
            }
            Log.i("CreateTrainingSession", "id_training_session: $trainingSessionSelected, week: $weekSelected, day: $daySelected")
            CreateSportSessionById(trainingSessionSelected,weekSelected,daySelected)
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

    private suspend fun getAllTrainingSessions(): List<TrainingSession> {
        val utils = Utils()
        return withContext(Dispatchers.IO) {
            try {
                val callGetAllTSessions = utils.getRetrofit(domain)
                    .create(ApiService::class.java)
                    .getTrainingSessionsById(user_id)
                    .execute()
                val getAllUserTrainingSessionsResponse = callGetAllTSessions.body()
                if (getAllUserTrainingSessionsResponse?.code == 200) {
                    getAllUserTrainingSessionsResponse.content ?: emptyList()
                } else {
                    emptyList()
                }
            } catch (e: Exception) {
                Log.e("error", e.message.toString())
                emptyList()
            }
        }
    }

    private suspend fun getTrainingPlanDetailById(training_plan_id: String): TrainingPlan? {
        val utils = Utils()
        return withContext(Dispatchers.IO) {
            try {
                val callGetTrainingPlanById = utils.getRetrofit(domain_trainning)
                    .create(ApiService::class.java)
                    .getTrainingPlan(training_plan_id)
                    .execute()
                val getTrainingPlan = callGetTrainingPlanById.body()
                if (getTrainingPlan?.code == 200) {
                    getTrainingPlan.training_plan
                } else {
                    null
                }
            } catch (e: Exception) {
                Log.e("error", e.message.toString())
                null
            }
        }
    }

    private suspend fun getAllSportSession(): List<SportSession>? {
        val utils = Utils()
        return withContext(Dispatchers.IO) {
            try {
                val callGetAllSportSessions = utils.getRetrofit(domain)
                    .create(ApiService::class.java)
                    .getSportUserSessions(user_id)
                    .execute()
                val getAllSportSessionResponse = callGetAllSportSessions.body() as GetAllSportSessionResponse?
                if (getAllSportSessionResponse?.code == 200){
                    Log.i("callGetAllSportSessions","Antes de refrescar la lista")
                    getAllSportSessionResponse?.content!!
                }else{
                    Log.e("getAllSportSessionResponse error: ",getAllSportSessionResponse?.message.toString())
                    null
                }
            } catch (e: Exception) {
                Log.e("error",e.message.toString())
                null
            }
        }
    }

    private fun fetchTrainingSessions() {
        CoroutineScope(Dispatchers.Main).launch {
            val trainingSessions = getAllTrainingSessions()
            if (_binding != null) {
                val filteredSessions = trainingSessions.filter { it.event_category == "plan_training" }
                val trainingPlanDetails = mutableListOf<TrainingPlan?>()

                // Realiza llamadas a API secuenciales para obtener detalles de cada sesión filtrada
                filteredSessions.forEach { session ->
                    val trainingPlanDetail = session.id_event?.let { getTrainingPlanDetailById(it) }
                    trainingPlanDetails.add(trainingPlanDetail)
                }

                val validTrainingPlans = trainingPlanDetails.filterNotNull()
                val userSportSessions = getAllSportSession()
                val planNames = mutableListOf<String>()
                val weeksAndDays = mutableMapOf<String, MutableList<Pair<Int, String>>>()
                validTrainingPlans.forEach { plan ->
                    val planName = plan.id+"-"+plan.name
                    if (planName != null) {
                        planNames.add(planName)
                        //planNames.add(plan.name.toString())
                        val availableWeeksAndDays = mutableListOf<Pair<Int, String>>()
                        for (week in 1..(plan.weeks?.toInt() ?: 1)) {
                            listOf(
                                "Lunes" to plan.lunes_enabled,
                                "Martes" to plan.martes_enabled,
                                "Miércoles" to plan.miercoles_enabled,
                                "Jueves" to plan.jueves_enabled,
                                "Viernes" to plan.viernes_enabled
                            ).forEach { (day, enabled) ->
                                if (enabled == 1) {
                                    val isDayAvailable = userSportSessions?.none { session ->
                                        var planId = session.name.toString().split("-")[0]
                                        planId == plan.id && session.week == week && session.day == day
                                    }
                                    if (isDayAvailable == true) {
                                        availableWeeksAndDays.add(Pair(week, day))
                                    }
                                }
                            }
                        }
                        weeksAndDays[planName] = availableWeeksAndDays
                    }
                }

                val spinnerPlanes = view?.findViewById<Spinner>(R.id.spinnerTrainingSessions)
                activity?.let {
                    ArrayAdapter(it, android.R.layout.simple_spinner_item, planNames).also { arrayAdapter ->
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinnerPlanes?.adapter = arrayAdapter
                    }
                }

                if (spinnerPlanes != null) {
                    spinnerPlanes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
                            setData("",0,"")
                            val selectedPlan = planNames[position]
                            val availableWeeksAndDays = weeksAndDays[selectedPlan] ?: emptyList()
                            updateWeeksAndDays(availableWeeksAndDays, selectedPlan, filteredSessions)
                        }
                        override fun onNothingSelected(p0: AdapterView<*>?) {}
                    }
                }

            }
        }
    }

    private fun updateWeeksAndDays(
        availableWeeksAndDays: List<Pair<Int, String>>,
        selectedPlan: String,
        filteredSessions: List<TrainingSession>
    ) {
        val spinnerWeeks = view?.findViewById<Spinner>(R.id.spinnerWeeks)
        val spinnerDays = view?.findViewById<Spinner>(R.id.spinnerDays)

        val weeks = availableWeeksAndDays.map { it.first }.distinct()

        activity?.let {
            ArrayAdapter(it, android.R.layout.simple_spinner_item, weeks).also { arrayAdapter ->
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerWeeks?.adapter = arrayAdapter
            }
        }

        if (weeks.isEmpty()) {
            updateDays(emptyList(), spinnerDays)
        }

        spinnerWeeks?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (weeks.isNotEmpty()) {
                    val selectedWeek = weeks[position]
                    val daysForWeek = availableWeeksAndDays.filter { it.first == selectedWeek }.map { it.second }
                    updateDays(daysForWeek, spinnerDays)

                    spinnerDays?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                            val selectedDay = daysForWeek[position]
                            val planId = selectedPlan.split("-")[0]
                            planName = selectedPlan.substringAfter("-")
                            val trainingSession = filteredSessions.find { it.id_event == planId}
                            trainingSession?.let {
                                val idTrainingSession = it.id
                                baseTrainingSession = it
                                // Aquí puedes manejar el idTrainingSession, week y day como necesites
                                Log.i("SelectedTrainingSession", "id_training_session: $idTrainingSession, week: $selectedWeek, day: $selectedDay")
                                setData(idTrainingSession.toString(),selectedWeek,selectedDay)
                            }
                        }
                        override fun onNothingSelected(parent: AdapterView<*>) {}
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun updateDays(days: List<String>, spinnerDays: Spinner?) {
        activity?.let {
            ArrayAdapter(it, android.R.layout.simple_spinner_item, days).also { arrayAdapter ->
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerDays?.adapter = arrayAdapter
            }
        }
    }

    private fun CreateSportSessionById(idTraining:String, weekS: Int, dayS:String){
        val utils = Utils()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val calendar = Calendar.getInstance()
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val event_date = sdf.format(calendar.time)
                val callCreateSportSession = utils.getRetrofit(domain)
                    .create(ApiService::class.java)
                    .postSportSession(SportSessionRequest(
                        idTraining,
                        weekS,
                        dayS,
                        "Bogota, Colombia",
                        event_date
                    ))
                    .execute()
                val createSportSession = callCreateSportSession.body()as GetAllSportSessionResponse?
                Log.i("Sali se la peticion createSportSession", "Rest")
                Log.i("Sali a la peticion code ", createSportSession?.code.toString())
                lifecycleScope.launch {
                    if (createSportSession?.code == 201) {
                        var sportSessions = createSportSession.content!!
                        var sportSession = sportSessions[0]
                        //ir a la siguiente pantalla
                        activity?.let {
                            mostrarSnackbar("Sesion Deportiva Creada exitosamente")
                            val bundle = bundleOf(
                                "sport_session_id" to sportSession.id,
                                "training_name" to planName
                            )
                            val mainActivity = requireActivity() as? MainActivity
                            mainActivity?.navigateToFragment(R.id.SportSessionStartFragment, "Sesion Deportiva", bundle)
                        }

                    } else {
                        activity?.let {
                            var msgError = ""
                            if (createSportSession != null) {
                                msgError = createSportSession.message.toString()
                            }
                            utils.showMessageDialog(
                                it,
                                "Error Al crear la sesion deportiva " + msgError
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("error",e.message.toString())
            }
        }

    }


}
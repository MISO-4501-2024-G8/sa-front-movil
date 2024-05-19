package com.miso202402.SportApp.fragments

import android.R
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.miso202402.SportApp.src.models.models.SportObjectiveSession
import com.miso202402.SportApp.src.models.models.SportSession
import com.miso202402.SportApp.src.models.request.RiskAlertsTrainingPlanRequest
import com.miso202402.SportApp.src.models.request.SportSessionPutRequest
import com.miso202402.SportApp.src.models.response.DeleteSportSessionResponse
import com.miso202402.SportApp.src.models.response.GetAllSportSessionResponse
import com.miso202402.SportApp.src.models.response.GetSportSessionResponse
import com.miso202402.SportApp.src.models.response.PutSportObjectiveSessionResponse
import com.miso202402.SportApp.src.models.response.PutSportSessionResponse
import com.miso202402.SportApp.src.models.response.RiskTrainingPlanResponse
import com.miso202402.SportApp.src.utils.ClickListener_SportObjectiveSession
import com.miso202402.SportApp.src.utils.SharedPreferences
import com.miso202402.SportApp.src.utils.SportObjectSessionAdapter
import com.miso202402.SportApp.src.utils.SportSessionAdapter
import com.miso202402.front_miso_pf2_g8_sportapp.activities.MainActivity
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentSportSessionStartBinding
import com.miso202402.front_miso_pf2_g8_sportapp.src.services.ApiService
import com.miso202402.front_miso_pf2_g8_sportapp.src.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale


class SportSessionStartFragment : Fragment(), ClickListener_SportObjectiveSession {

    private var _binding: FragmentSportSessionStartBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferences: SharedPreferences
    private var domain: String = "https://g7o4mxf762.execute-api.us-east-1.amazonaws.com/prod/"
    private lateinit var sport_session_id: String;
    private lateinit var training_name: String;
    private lateinit var user_id: String;
    lateinit var typePlan : String
    private lateinit var sportSession: SportSession
    lateinit var listener:ClickListener_SportObjectiveSession
    private var seconds = 0
    private var secondsTargets = 0
    private var running = false
    private val wasRunning = false
    private var currentObjectiveIndex = 0


    override fun onAttach(context: Context) {
        super.onAttach(context)
        preferences = SharedPreferences(requireContext())
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSportSessionStartBinding.inflate(inflater, container, false)
        sport_session_id = arguments?.getString("sport_session_id").toString()
        training_name = arguments?.getString("training_name").toString()
        user_id = preferences.getData<String>("id").toString()
        typePlan = preferences.getData<String>("typePlan").toString()
        Log.i("user_id", user_id)
        Log.i("sport_session_id", sport_session_id)
        Log.i("typePlan", typePlan)
        Log.i("training_name", training_name)
        listener = this
        getSportSessionById(sport_session_id)
        binding.buttonIniciar.setOnClickListener(){
            onClickStart(container)
        }
        binding.buttonPausar.setOnClickListener(){
            onClickStop(container)
        }
        binding.buttonFinalizar.setOnClickListener(){
            onClickStop(container)
            if(sportSession.objective_instructions !== null) {
                finalizarSession()
            }
            //onClickReset(container)
        }
        binding.buttonAbandonar.setOnClickListener(){
            // Crea un AlertDialog.Builder y configura el mensaje y los botones
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("¿Estás seguro de que deseas abandonar la sesión?")
                .setCancelable(false)
                .setPositiveButton("Sí") { dialog, id ->
                    // Si el usuario confirma, ejecuta la función de abandono de sesión
                    //abandonarSesion()
                    onClickStop(container)
                    if(sportSession.objective_instructions !== null) {
                        finalizarSession()
                    }
                }
                .setNegativeButton("No") { dialog, id ->
                    // Si el usuario cancela, simplemente cierra el diálogo
                    dialog.dismiss()
                }

            // Muestra el diálogo
            val alert = builder.create()
            alert.show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun onClickStart(view: View?) {
        running = true
    }
    fun onClickStop(view: View?) {
        running = false
    }

    fun onClickReset(view: View?) {
        running = false
        seconds = 0
        secondsTargets = 0
    }

    private fun runTimer() {
        val timeView = binding.timeView
        val handler = Handler()

        handler.post(object : Runnable {
            override fun run() {
                val hours = seconds / 3600
                val minutes = seconds % 3600 / 60
                val secs = seconds % 60

                val time = String.format(
                    Locale.getDefault(),
                    "%d:%02d:%02d", hours,
                    minutes, secs
                )
                timeView.text = time

                // Obtener el objetivo actual
                val currentObjective = sportSession.objective_instructions?.getOrNull(currentObjectiveIndex)

                // Verificar si hay un objetivo actual y si se ha cumplido el tiempo
                currentObjective?.let { objective ->
                    var instructionT = objective.instruction_time?.times(60)
                    if (secondsTargets >= instructionT!! && objective.target_achieved == 0) {
                        objective.target_achieved = 1 // Cambiar target_achieved a 1
                        mostrarSnackbar("Objetivo cumplido: " + objective.instruction_description)
                        // Notificar al adaptador que los datos han cambiado
                        binding.recyclerviewListObjectives.adapter?.notifyItemChanged(currentObjectiveIndex)

                        // Incrementar el índice para pasar al siguiente objetivo en la siguiente iteración
                        currentObjectiveIndex++

                        // Reiniciar el temporizador para el siguiente objetivo si hay más
                        if (currentObjectiveIndex < sportSession.objective_instructions!!.size) {
                            secondsTargets = 0 // Reiniciar el contador de segundos para el siguiente objetivo
                        }
                    }
                }

                if (running) {
                    seconds++
                    secondsTargets++
                }
                handler.postDelayed(this, 1000) // Ejecutar cada segundo (1000 milisegundos)
            }
        })
    }

    private fun getSportSessionById(sport_session_id: String){
        val utils = Utils()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val callGetSportSession = utils.getRetrofit(domain)
                    .create(ApiService::class.java)
                    .getSportSessionById(sport_session_id)
                    .execute()
                val getSportSession = callGetSportSession.body()as GetSportSessionResponse?
                Log.i("Sali se la peticion getSportSession", "Rest")
                Log.i("Sali a la peticion code ", getSportSession?.code.toString())
                lifecycleScope.launch {
                    if (getSportSession?.code == 200) {
                        sportSession = getSportSession.content!!
                        binding.nameSession.text = training_name
                        binding.weekSession.text = "Semana: " + sportSession.week.toString()
                        binding.daySession.text = "Dia: " + sportSession.day
                        binding.recyclerviewListObjectives.setHasFixedSize(true)
                        binding.recyclerviewListObjectives.layoutManager =
                            LinearLayoutManager(context)
                        binding.recyclerviewListObjectives.adapter =
                            sportSession.objective_instructions?.let {
                                SportObjectSessionAdapter(
                                    it,
                                    listener
                                )
                            }
                        runTimer()
                    } else {
                        activity?.let {
                            utils.showMessageDialog(
                                it,
                                "Error Al traer el evento, intente mas tarde"
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("error",e.message.toString())
            }
        }

    }

    override fun onCListItemClick(view: View, sportObjectiveSession: SportObjectiveSession){
        Log.i("sportObjectiveSession Item: ", sportObjectiveSession.id.toString())
    }

    fun finalizarSession(){
        val utils = Utils()
        val filteredInstructions = sportSession.objective_instructions?.filter { it.target_achieved == 1}
        CoroutineScope(Dispatchers.IO).launch {
            try{
                if (filteredInstructions != null) {
                    filteredInstructions.forEach { instruction ->
                        updateObjective(instruction,utils)
                    }
                }
                updateSportSession(sport_session_id,utils)
            }catch (e: Exception) {
                Log.e("Updating objective error: ",e.message.toString())
            }
            activity?.let {
                withContext(Dispatchers.Main) {
                    mostrarSnackbar("Sesion actualizada exitosamente")
                    val mainActivity = requireActivity() as? MainActivity
                    mainActivity?.navigateToFragment(com.miso202402.front_miso_pf2_g8_sportapp.R.id.SportFragment, "Sesion Deportiva")
                }

            }
        }

    }

    private fun updateObjective(objective: SportObjectiveSession, utils: Utils){
        val callUpdateSessionObjective = utils.getRetrofit(domain)
            .create(ApiService::class.java)
            .putSportSessionById(
                objective.id.toString(),
                objective
            ).execute()
        val callUpdateSportObjectiveSessionResponse = callUpdateSessionObjective.body() as PutSportObjectiveSessionResponse?
        Log.i("updateObjective",callUpdateSportObjectiveSessionResponse?.code.toString())
        if(callUpdateSportObjectiveSessionResponse?.code == 200){
            Log.i("updateObjective","Objetivo actualizado")
        }else{
            var errorMessage = callUpdateSportObjectiveSessionResponse?.message.toString()
            Log.e("updateObjective error",errorMessage)
            throw Exception("Error al actualizar objetivo: $errorMessage")
        }

    }

    private fun updateSportSession(idSportSession:String, utils: Utils){
        val callUpdateSportSession = utils.getRetrofit(domain)
            .create(ApiService::class.java)
            .putSportSessionById(
                idSportSession,
                SportSessionPutRequest(seconds)
            ).execute()
        val callUpdateSportSessionResponse = callUpdateSportSession.body() as PutSportSessionResponse?
        Log.i("updateSportSession",callUpdateSportSessionResponse?.code.toString())
        if(callUpdateSportSessionResponse?.code == 200){
            Log.i("updateSportSession","Sesion actualizada")
        }else{
            var errorMessage = callUpdateSportSessionResponse?.message.toString()
            Log.e("updateSportSession error",errorMessage)
            throw Exception("Error al actualizar sesion: $errorMessage")
        }
    }

    /*
    fun abandonarSesion() {
        val utils = Utils()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val callDeleteSportSessions = utils.getRetrofit(domain)
                    .create(ApiService::class.java)
                    .deleteSportSessionById(sport_session_id)
                    .execute()
                val deleteSportSessionResponse = callDeleteSportSessions.body() as DeleteSportSessionResponse?
                if (deleteSportSessionResponse?.code == 200){
                    Log.i("callDeleteSportSessions",deleteSportSessionResponse.message.toString())
                }
            } catch (e: Exception) {
                Log.e("error",e.message.toString())
            }
            activity?.let {
                withContext(Dispatchers.Main) {
                    mostrarSnackbar("Sesion abandonada exitosamente")
                    val mainActivity = requireActivity() as? MainActivity
                    mainActivity?.navigateToFragment(com.miso202402.front_miso_pf2_g8_sportapp.R.id.SportFragment, "Sesion Deportiva")
                }

            }
        }
    }
    */
}
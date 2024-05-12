package com.miso202402.SportApp.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.miso202402.SportApp.src.models.models.Events
import com.miso202402.SportApp.src.models.models.Routs
import com.miso202402.SportApp.src.models.models.TrainingSession
import com.miso202402.SportApp.src.models.response.GetAllEventsResponse
import com.miso202402.SportApp.src.models.response.GetAllRutasResponse
import com.miso202402.SportApp.src.models.response.GetAllUserTrainingSessionsResponse
import com.miso202402.SportApp.src.utils.ClicTSListener
import com.miso202402.SportApp.src.utils.SharedPreferences
import com.miso202402.SportApp.src.utils.TrainingSessionAdapter
import com.miso202402.front_miso_pf2_g8_sportapp.R
import com.miso202402.front_miso_pf2_g8_sportapp.activities.MainActivity
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentCalendarBinding
import com.miso202402.front_miso_pf2_g8_sportapp.src.services.ApiService
import com.miso202402.front_miso_pf2_g8_sportapp.src.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


/**
 * A simple [Fragment] subclass.
 * Use the [CalendarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalendarFragment : Fragment(), ClicTSListener {
    private var _binding: FragmentCalendarBinding? = null
    private lateinit var user_id: String;
    private lateinit var preferences: SharedPreferences


    private val binding get() = _binding!!
    private lateinit var trainingSessions : List<TrainingSession>
    private lateinit var filterTrainingSessions : List<TrainingSession>
    private lateinit var events : List<Events>
    private lateinit var routes : List<Routs>
    private var vectorTipoDeporte  =  arrayOf("Atletismo", "Ciclismo")
    private var vectorTipoSesion  =  arrayOf("Virtual", "Presencial")
    private var domain: String = "https://g7o4mxf762.execute-api.us-east-1.amazonaws.com/prod/"
    lateinit var listener: ClicTSListener
    lateinit var datePicker: DatePicker
    lateinit var btnLimpiar: Button
    lateinit var btnEventos : Button
    lateinit var btnRutas : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        preferences = SharedPreferences(requireContext())
    }

    fun convertirFechaAFormatoISO8601(fecha: Date): String {
        val formato = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        return formato.format(fecha)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        trainingSessions = listOf()
        filterTrainingSessions = listOf()
        events = listOf()
        routes = listOf()
        listener = this
        user_id = preferences.getData<String>("id").toString()
        Log.i("user_id", user_id)
        datePicker = binding.datePickerF
        resetDateToToday()
        datePicker.setOnDateChangedListener { _, year, month, dayOfMonth ->
            try {
                progressBarVisible(true)
                // Construir la fecha seleccionada
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                val fechaFormateada =
                    convertirFechaAFormatoISO8601(selectedDate.time).substring(0, 10)
                filterTrainingSessions = trainingSessions
                val datosFiltrados = filterTrainingSessions.filter { item ->
                    val itemDate = item.session_date.toString().substring(0, 10)
                    itemDate == fechaFormateada
                }
                filterTrainingSessions = datosFiltrados
                binding.recyclerviewListTrainingSessionsFragment.setHasFixedSize(true)
                binding.recyclerviewListTrainingSessionsFragment.layoutManager =
                    LinearLayoutManager(context)
                binding.recyclerviewListTrainingSessionsFragment.adapter =
                    TrainingSessionAdapter(filterTrainingSessions, events, routes, listener)
                progressBarVisible(false)
            }catch (e: Exception) {
                Log.e("error filtrando datos: ", e.message.toString())
                progressBarVisible(false)
            }
        }
        btnEventos = binding.btnPEvento
        btnRutas = binding.btnPRuta
        btnLimpiar = binding.btnLimpiar

        btnEventos.setOnClickListener{
            val mainActivity = requireActivity() as? MainActivity
            mainActivity?.navigateToFragment(R.id.ListEventsFragment, "Eventos")
        }
        btnRutas.setOnClickListener{
            val mainActivity = requireActivity() as? MainActivity
            mainActivity?.navigateToFragment(R.id.ListRoutsFragment, "Rutas")
        }
        btnLimpiar.setOnClickListener {
            progressBarVisible(true)
            resetDateToToday()
            filterTrainingSessions = trainingSessions
            binding.recyclerviewListTrainingSessionsFragment.setHasFixedSize(true)
            binding.recyclerviewListTrainingSessionsFragment.layoutManager = LinearLayoutManager(context)
            binding.recyclerviewListTrainingSessionsFragment.adapter = TrainingSessionAdapter(filterTrainingSessions, events, routes, listener)
            progressBarVisible(false)
        }
        return binding.root
    }

    fun resetDateToToday() {
        val cal = Calendar.getInstance()
        datePicker.updateDate(
            cal[Calendar.YEAR],
            cal[Calendar.MONTH] ,
            cal[Calendar.DAY_OF_MONTH]
        )
    }

    fun progressBarVisible(valueV:Boolean){
        if(valueV){
            binding.progressBar.visibility = View.VISIBLE

        }else{
            binding.progressBar.visibility = View.GONE
        }
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

    private suspend fun getAllTrainingSessions(): List<TrainingSession> {
        val utils = Utils()
        return withContext(Dispatchers.IO) {
            try {
                val callGetAllTSessions = utils.getRetrofit(domain)
                    .create(ApiService::class.java)
                    .getTrainingSessionsById(user_id)
                    .execute()
                val GetAllUserTrainingSessionsResponse = callGetAllTSessions.body() as GetAllUserTrainingSessionsResponse?
                Log.i("GetAllUserTrainingSessionsResponse: ", GetAllUserTrainingSessionsResponse?.code.toString())
                if (GetAllUserTrainingSessionsResponse?.code == 200) {
                    return@withContext GetAllUserTrainingSessionsResponse?.content ?: emptyList()
                } else {
                    return@withContext emptyList()
                }
            } catch (e: Exception) {
                Log.e("error", e.message.toString())
                return@withContext emptyList()
            }
        }
    }

    private suspend fun getAllEvents(): List<Events> {
        val utils = Utils()
        return withContext(Dispatchers.IO) {
            try {
                val callGetAllTSessions = utils.getRetrofit(domain)
                    .create(ApiService::class.java)
                    .getAllEventos()
                    .execute()
                val GetAllEventsResponse = callGetAllTSessions.body() as GetAllEventsResponse?
                Log.i("GetAllEventsResponse: ", GetAllEventsResponse?.code.toString())
                if (GetAllEventsResponse?.code == 200) {
                    return@withContext GetAllEventsResponse?.content ?: emptyList()
                } else {
                    return@withContext emptyList()
                }
            } catch (e: Exception) {
                Log.e("error", e.message.toString())
                return@withContext emptyList()
            }
        }
    }

    private suspend fun getAllRoutes(): List<Routs> {
        val utils = Utils()
        return withContext(Dispatchers.IO) {
            try {
                val callGetAllTSessions = utils.getRetrofit(domain)
                    .create(ApiService::class.java)
                    .getAllRutas()
                    .execute()
                val GetAllRutasResponse = callGetAllTSessions.body() as GetAllRutasResponse?
                Log.i("GetAllRutasResponse: ", GetAllRutasResponse?.code.toString())
                if (GetAllRutasResponse?.code == 200) {
                    return@withContext GetAllRutasResponse?.content ?: emptyList()
                } else {
                    return@withContext emptyList()
                }
            } catch (e: Exception) {
                Log.e("error", e.message.toString())
                return@withContext emptyList()
            }
        }
    }

    private fun fetchTrainingSessions() {
        progressBarVisible(true)
        CoroutineScope(Dispatchers.Main).launch {
            trainingSessions = getAllTrainingSessions()
            filterTrainingSessions = trainingSessions
            events = getAllEvents()
            routes = getAllRoutes()
            Log.i("GetAllUserTrainingSessionsResponse: ", trainingSessions.size.toString())
            Log.i("GetAllEventsResponse: ", events.size.toString())
            Log.i("GetAllRutasResponse: ", routes.size.toString())
            if(_binding != null) {
                binding.recyclerviewListTrainingSessionsFragment.setHasFixedSize(true)
                binding.recyclerviewListTrainingSessionsFragment.layoutManager =
                    LinearLayoutManager(context)
                binding.recyclerviewListTrainingSessionsFragment.adapter =
                    TrainingSessionAdapter(filterTrainingSessions, events, routes, listener)
                progressBarVisible(false)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.showToolbarAndFab()
        fetchTrainingSessions()
    }

    override fun onCListTSItemClic(view: View, trainingSession: TrainingSession) {
        Log.i("Prueba", trainingSession.id_event.toString())
        if(trainingSession.event_category == "evento") {
            val bundle = bundleOf("event_id" to trainingSession.id_event)
            val mainActivity = requireActivity() as? MainActivity
            mainActivity?.navigateToFragment(R.id.InfoEventFragment, "Detalle Evento", bundle)
        }else if(trainingSession.event_category == "ruta"){
            val bundle = bundleOf("rout_id" to trainingSession.id_event)
            val mainActivity = requireActivity() as? MainActivity
            mainActivity?.navigateToFragment(R.id.InfoRoutFragment, "Detalle Ruta", bundle)
        }
    }

}
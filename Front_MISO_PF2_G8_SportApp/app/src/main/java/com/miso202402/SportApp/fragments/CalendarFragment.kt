package com.miso202402.SportApp.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.annotation.RequiresExtension
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
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
    private lateinit var events : List<Events>
    private lateinit var routes : List<Routs>
    private var vectorTipoDeporte  =  arrayOf("Atletismo", "Ciclismo")
    private var vectorTipoSesion  =  arrayOf("Virtual", "Presencial")
    private var domain: String = "https://g7o4mxf762.execute-api.us-east-1.amazonaws.com/prod/"
    lateinit var listener: ClicTSListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        preferences = SharedPreferences(requireContext())
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        trainingSessions = listOf()
        events = listOf()
        routes = listOf()
        listener = this
        user_id = preferences.getData<String>("id").toString()
        Log.i("user_id", user_id)
        return binding.root
    }

    fun progressBarVisible(valueV:Boolean){
        if(valueV){
            binding.progressBar.visibility = View.VISIBLE

        }else{
            binding.progressBar.visibility = View.GONE
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
            events = getAllEvents()
            routes = getAllRoutes()
            Log.i("GetAllUserTrainingSessionsResponse: ", trainingSessions.size.toString())
            Log.i("GetAllEventsResponse: ", events.size.toString())
            Log.i("GetAllRutasResponse: ", routes.size.toString())
            binding.recyclerviewListTrainingSessionsFragment.setHasFixedSize(true)
            binding.recyclerviewListTrainingSessionsFragment.layoutManager = LinearLayoutManager(context)
            binding.recyclerviewListTrainingSessionsFragment.adapter = TrainingSessionAdapter(trainingSessions, events, routes, listener)
            progressBarVisible(false)
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
            mainActivity?.navigateToFragment(R.id.InfoEventFragment, bundle)
        }else if(trainingSession.event_category == "ruta"){
            val bundle = bundleOf("rout_id" to trainingSession.id_event)
            val mainActivity = requireActivity() as? MainActivity
            mainActivity?.navigateToFragment(R.id.InfoRoutFragment, bundle)
        }
    }

}
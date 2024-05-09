package com.miso202402.SportApp.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresExtension
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.miso202402.SportApp.src.models.models.TrainingSession
import com.miso202402.SportApp.src.models.response.GetAllUserTrainingSessionsResponse
import com.miso202402.SportApp.src.utils.ClicTSListener
import com.miso202402.SportApp.src.utils.SharedPreferences
import com.miso202402.SportApp.src.utils.TrainingSessionAdapter
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
        listener = this
        user_id = preferences.getData<String>("id").toString()
        Log.i("user_id", user_id)
        return binding.root
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

    private fun fetchTrainingSessions() {
        CoroutineScope(Dispatchers.Main).launch {
            trainingSessions = getAllTrainingSessions()
            Log.i("GetAllUserTrainingSessionsResponse: ", trainingSessions.size.toString())
            binding.recyclerviewListTrainingSessionsFragment.setHasFixedSize(true)
            binding.recyclerviewListTrainingSessionsFragment.layoutManager = LinearLayoutManager(context)
            binding.recyclerviewListTrainingSessionsFragment.adapter = TrainingSessionAdapter(trainingSessions, listener)
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
        val bundle = bundleOf("event_id" to trainingSession.id_event )
        //val mainActivity = requireActivity() as? MainActivity
        //mainActivity?.navigateToFragment(R.id.action_ListEventsFragment_to_EditEventsFragment, bundle)
    }

}
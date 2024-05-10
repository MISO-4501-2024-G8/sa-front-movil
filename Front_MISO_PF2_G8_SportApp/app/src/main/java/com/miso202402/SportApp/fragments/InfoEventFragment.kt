package com.miso202402.SportApp.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.miso202402.SportApp.src.models.models.Events
import com.miso202402.SportApp.src.models.request.EventsRequest
import com.miso202402.SportApp.src.models.request.TrainingSessionRequest
import com.miso202402.SportApp.src.models.response.GetEventResponse
import com.miso202402.SportApp.src.models.response.TraingSessionResponse
import com.miso202402.SportApp.src.utils.SharedPreferences
import com.miso202402.front_miso_pf2_g8_sportapp.R
import com.miso202402.front_miso_pf2_g8_sportapp.activities.MainActivity
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentInfoEventBinding
import com.miso202402.front_miso_pf2_g8_sportapp.src.services.ApiService
import com.miso202402.front_miso_pf2_g8_sportapp.src.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class InfoEventFragment : Fragment() {
    private var _binding: FragmentInfoEventBinding? = null
    private lateinit var preferences: SharedPreferences

    private var domain: String = "https://g7o4mxf762.execute-api.us-east-1.amazonaws.com/prod/"
    private lateinit var event_id: String;
    private lateinit var user_id: String;
    private var vectorTipoDeporte  =  arrayOf("Atletismo", "Ciclismo")
    private lateinit var event: Events
    private var tipoDeporte : String? = null
    private var event_date : String? = null

    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        preferences = SharedPreferences(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoEventBinding.inflate(inflater, container, false)
        event_id = arguments?.getString("event_id").toString()
        //user_id = arguments?.getString("user_id").toString()
        Log.i("event_id", event_id)
        user_id = preferences.getData<String>("id").toString()
        Log.i("user_id", user_id)
        event = Events("", "","","","", "","", "")
        getEventById(event_id)
        binding.buttonAtrasEventsFragment.setOnClickListener(){
            val mainActivity = requireActivity() as? MainActivity
            mainActivity?.navigateToFragment(R.id.CalendarFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getEventById(event_id: String){
        val utils = Utils()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val callGetAllEventos = utils.getRetrofit(domain)
                    .create(ApiService::class.java)
                    .getEventoById(event_id)
                    .execute()
                val getEvento = callGetAllEventos.body()as GetEventResponse?
                Log.i("Sali se la peticion getEventoById", "Rest")
                Log.i("Sali a la peticion code ", getEvento?.code.toString())
                lifecycleScope.launch {
                    if (getEvento?.code == 200) {
                        event = getEvento.content!!
                        binding.editTexNameEventsFragment.setText(event.event_name.toString())
                        binding.editTexDescriptionEventsFragment.setText(event.event_name.toString())
                        binding.editTexLocationEventsFragment.setText(event.event_location.toString())
                        binding.editLinkEventsFragment.setText(event.link.toString())
                        binding.sportEventsFragment.setText(event.sport.toString())
                        binding.dateEventsFragment.setText(event.event_date.toString())
                        event_date = event.event_date.toString()
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


}
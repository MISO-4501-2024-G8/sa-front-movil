package com.miso202402.SportApp.fragments

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
import com.miso202402.front_miso_pf2_g8_sportapp.R
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentEditEventsBinding
import com.miso202402.front_miso_pf2_g8_sportapp.src.services.ApiService
import com.miso202402.front_miso_pf2_g8_sportapp.src.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class EditEventsFragment : Fragment() {
    private var _binding: FragmentEditEventsBinding? = null

    private var domain: String = "https://g7o4mxf762.execute-api.us-east-1.amazonaws.com/prod/"
    private lateinit var event_id: String;
    private lateinit var user_id: String;
    private var vectorTipoDeporte  =  arrayOf("Atletismo", "Ciclismo")
    private lateinit var event: Events
    private var tipoDeporte : String? = null

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditEventsBinding.inflate(inflater, container, false)
        event_id = arguments?.getString("event_id").toString()
        user_id = arguments?.getString("user_id").toString()
        Log.i("event_id", event_id)
        Log.i("user_id", user_id)
        event = Events("", "","","","", "", "")
        getEventById(event_id)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var spinner = view.findViewById<Spinner>(R.id.spinner_EditEventsFragment)
        activity?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.Sport,
                android.R.layout.simple_spinner_item
            ).also { arrayAdapter ->
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = arrayAdapter
            }
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                tipoDeporte = vectorTipoDeporte[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.buttonEditarEditEventsFragment.setOnClickListener {
           //updateEventoById(event_id)
            createTrainigSession(event_id, user_id)
        }
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
                    binding.editTexNameEditEventsFragment.setText(event.event_name.toString())
                    binding.editTexDescriptionEditEventsFragment.setText(event.event_description.toString())
                    binding.editTexLocationEditEventsFragment.setText(event.event_location.toString())
                    binding.editLinkEditEventsFragment.setText(event.link.toString())
                    if (event.sport == "Atletismo"){
                        binding.spinnerEditEventsFragment.setSelection(0)
                        tipoDeporte = binding.spinnerEditEventsFragment.setSelection(0).toString()
                    } else {
                        binding.spinnerEditEventsFragment.setSelection(1)
                        tipoDeporte = binding.spinnerEditEventsFragment.setSelection(1).toString()
                    }
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

    private fun updateEventoById(event_id: String){
        val utils = Utils()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.i("Entre", "update")
                val callGetAllEventos = utils.getRetrofit(domain)
                    .create(ApiService::class.java)
                    .updateEventoById(event_id,
                        EventsRequest(
                            binding.editTexNameEditEventsFragment.text.toString(),
                            binding.editTexDescriptionEditEventsFragment.text.toString(),
                            binding.editTexLocationEditEventsFragment.text.toString(),
                            event.event_type,
                            tipoDeporte,
                            binding.editLinkEditEventsFragment.text.toString(),
                            "2024-05-28 14:30:00")
                    )
                    .execute()
                val updateEvento = callGetAllEventos.body() as GetEventResponse?
                Log.i("Sali se la peticion updateEventoById", "Rest")
                Log.i("Sali a la peticion code ", updateEvento?.code.toString())
                lifecycleScope.launch {
                    if (updateEvento?.code == 200) {
                        val messageSucces = updateEvento.message
                        utils.showMessageDialog(context, messageSucces.toString())
                        findNavController().navigate(R.id.action_EditEventsFragment_to_ListEventsFragment)
                    } else {
                        activity?.let {
                            utils.showMessageDialog(
                                it,
                                "Error Al traer el evento para editar, intente mas tarde"
                            )
                        }

                    }
                }
            } catch (e: Exception) {
                Log.e("error",e.message.toString())
            }
        }

    }

    private fun createTrainigSession(event_id: String, user_id: String){
        val utils = Utils()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.i("Entre", "update")
                val callCreateTrainigSession = utils.getRetrofit(domain)
                    .create(ApiService::class.java)
                    .createTrainigSession(
                        TrainingSessionRequest(
                            user_id,
                            event_id,
                            event.event_type,
                            tipoDeporte,
                            "2024-05-28 14:30:00"
                        )
                    )
                    .execute()
                val createSession = callCreateTrainigSession.body() as TraingSessionResponse?
                Log.i("Sali se la peticion createSession", "Rest")
                Log.i("Sali a la peticion code ", createSession?.code.toString())
                lifecycleScope.launch {
                    if (createSession?.code == 201) {
                        val messageSucces = createSession.message
                        utils.showMessageDialog(context, messageSucces.toString())
                        findNavController().navigate(R.id.action_EditEventsFragment_to_ListEventsFragment)
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
                Log.e("error",e.message.toString())
            }
        }

    }


}
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
import com.miso202402.SportApp.src.models.response.GetEventResponse
import com.miso202402.front_miso_pf2_g8_sportapp.R
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentAddEventBinding
import com.miso202402.front_miso_pf2_g8_sportapp.src.services.ApiService
import com.miso202402.front_miso_pf2_g8_sportapp.src.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AddEventFragment : Fragment() {
    private var _binding: FragmentAddEventBinding? = null
    private val binding get() = _binding!!

    private var domain: String = "https://g7o4mxf762.execute-api.us-east-1.amazonaws.com/prod/"
    private var vectorTipoDeporte  =  arrayOf("Atletismo", "Ciclismo")
    private var tipoDeporte : String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var spinner = view.findViewById<Spinner>(R.id.spinner_AddEventFragment)
        activity?.let {
            ArrayAdapter.createFromResource(it,
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
                Log.i("mesnaje al selecionar tipo de deporte ", tipoDeporte.toString())
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        binding.buttonAgregarAddEventFragment.setOnClickListener {
            if(binding.editTexNameAddEventFragment.text.toString() != "" ||
                binding.editTexDescriptionAddEventFragment.text.toString() != "" ||
                binding.editTexLocationAddEventFragment.text.toString() != "" ||
                binding.editLinkAddEventFragment.text.toString() != "" ){

                var event = Events( "",
                    binding.editTexNameAddEventFragment.text.toString(),
                    binding.editTexDescriptionAddEventFragment.text.toString(),
                    binding.editTexLocationAddEventFragment.text.toString(),
                    "virtual",
                    "",
                    tipoDeporte,
                    binding.editLinkAddEventFragment.text.toString()
                )
                Log.i("Entre al boton", "Boton add")
                createEvent(event)
            }
            else{
                lifecycleScope.launch {
                    val util = Utils()
                    val message = "Alguno de los campos estas vacios"
                    util.showMessageDialog(context, message)
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun createEvent(event : Events){
        val utils = Utils()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val callGetAllEventos = utils.getRetrofit(domain)
                    .create(ApiService::class.java)
                    .createEventos(EventsRequest(
                            event.event_name,
                            event.event_description,
                            event.event_location,
                            event.event_type,
                            event.sport,
                            event.link,
                        "2024-05-28 14:30:00"
                            ))
                    .execute()
                val createEvento = callGetAllEventos.body()as GetEventResponse?
                Log.i("Sali a la perticion", "Rest")
                Log.i("Sali a la perticion code ", createEvento?.code.toString())
                if (createEvento?.code == 201){
                    lifecycleScope.launch {
                        Log.i("Rest CreateEvent:", createEvento?.code.toString())
                        activity?.let {
                            utils.showMessageDialog(
                                it,
                                createEvento?.message.toString()
                            )
                        }
                        findNavController().navigate(R.id.action_AddEventFragment_to_ListEventsFragment)
                    }
                }
                else {
                    lifecycleScope.launch {
                        activity?.let {
                            utils.showMessageDialog(
                                it,
                                "Error Intente mas tarde"
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
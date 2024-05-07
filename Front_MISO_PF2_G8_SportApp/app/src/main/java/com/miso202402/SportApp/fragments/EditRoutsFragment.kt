package com.miso202402.SportApp.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.RequiresExtension
import androidx.lifecycle.lifecycleScope
import com.miso202402.SportApp.src.models.models.Events
import com.miso202402.SportApp.src.models.models.Routs
import com.miso202402.SportApp.src.models.response.GetEventResponse
import com.miso202402.SportApp.src.models.response.GetRoutsResponse
import com.miso202402.front_miso_pf2_g8_sportapp.R
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentAddRoutsBinding
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentEditRoutsBinding
import com.miso202402.front_miso_pf2_g8_sportapp.src.services.ApiService
import com.miso202402.front_miso_pf2_g8_sportapp.src.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class EditRoutsFragment : Fragment() {
    private var _binding: FragmentEditRoutsBinding? = null
    private val binding get() = _binding!!
    private var domain: String = "https://g7o4mxf762.execute-api.us-east-1.amazonaws.com/prod/"
    private var vectorTipoDeporte  =  arrayOf("Atletismo", "Ciclismo")
    private var tipoDeporte : String? = null
    private lateinit var rout: Routs
    private lateinit var route_id: String;

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditRoutsBinding.inflate(inflater, container, false)
        route_id = arguments?.getString("event_id").toString()
        Log.i("event_id", route_id)
        rout = Routs("", "","","","",
            "", "", "", "","","")

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
        binding.buttonAgregarEditRoutsFragment.setOnClickListener {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getRoutById(event_id: String){
        val utils = Utils()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val callGetRutaById = utils.getRetrofit(domain)
                    .create(ApiService::class.java)
                    .getRutaById(event_id)
                    .execute()
                val getRuta = callGetRutaById.body()as GetRoutsResponse?
                Log.i("Sali se la peticion getRuta", "Rest")
                Log.i("Sali a la peticion code ", getEvento?.code.toString())
                lifecycleScope.launch {
                    if (getRuta?.code == 200) {
                        rout = getRuta.content!!
                        binding.editTexNameEditEventsFragment.setText(event.event_name.toString())
                        binding.editTexDescriptionEditEventsFragment.setText(event.event_description.toString())
                        binding.editTexLocationEditEventsFragment.setText(event.event_location.toString())
                        binding.editLinkEditEventsFragment.setText(event.link.toString())
                        if (event.sport == "Atletismo") binding.spinnerEditEventsFragment.setSelection(0)
                        else binding.spinnerEditEventsFragment.setSelection(1)
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
}
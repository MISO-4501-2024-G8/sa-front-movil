package com.miso202402.SportApp.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.RequiresExtension
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.miso202402.SportApp.src.models.models.Routs
import com.miso202402.SportApp.src.models.request.RoutsRequest
import com.miso202402.SportApp.src.models.response.GetEventResponse
import com.miso202402.SportApp.src.models.response.GetRoutsResponse
import com.miso202402.front_miso_pf2_g8_sportapp.R
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentAddRoutsBinding
import com.miso202402.front_miso_pf2_g8_sportapp.src.services.ApiService
import com.miso202402.front_miso_pf2_g8_sportapp.src.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AddRoutsFragment : Fragment() {

    private var _binding: FragmentAddRoutsBinding? = null
    private val binding get() = _binding!!

    private var domain: String = "https://g7o4mxf762.execute-api.us-east-1.amazonaws.com/prod/"
    private var vectorTipoDeporte  =  arrayOf("Atletismo", "Ciclismo")
    private var tipoDeporte : String? = null

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddRoutsBinding.inflate(inflater, container, false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var spinner = view.findViewById<Spinner>(R.id.spinner_AddRoutsFragment)
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

        binding.buttonAgregarAddRoutsFragment.setOnClickListener {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createRout(event : Routs){
        val utils = Utils()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val callCreateRuta = utils.getRetrofit(domain)
                    .create(ApiService::class.java)
                    .createRuta(
                        RoutsRequest(
                            event.route_name,
                            event.route_description,
                            event.route_location_A,
                            event.route_location_B,
                            event.route_latlon_A,
                            event.route_latlon_B,
                            "Virtual",
                            "2024-05-28 14:30:00",
                            event.sport,
                            event.link,
                            )
                    )
                    .execute()
                val createRuta = callCreateRuta.body()as GetRoutsResponse?
                Log.i("Sali a la perticion", "Rest")
                Log.i("Sali a la perticion code ", createRuta?.code.toString())
                if (createRuta?.code == 201){
                    lifecycleScope.launch {
                        Log.i("Rest CreateRout:", createRuta?.code.toString())
                        activity?.let {
                            utils.showMessageDialog(
                                it,
                                createRuta?.message.toString()
                            )
                        }

                        findNavController().navigate(R.id.action_AddRoutsFragment_to_ListRoutsFragment)
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
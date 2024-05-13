package com.miso202402.SportApp.fragments

import android.content.Context
import android.os.Build
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
import androidx.annotation.RequiresExtension
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.miso202402.SportApp.src.models.models.Events
import com.miso202402.SportApp.src.models.models.Routs
import com.miso202402.SportApp.src.models.request.TrainingSessionRequest
import com.miso202402.SportApp.src.models.response.GetEventResponse
import com.miso202402.SportApp.src.models.response.GetRoutsResponse
import com.miso202402.SportApp.src.models.response.TraingSessionResponse
import com.miso202402.SportApp.src.utils.SharedPreferences
import com.miso202402.front_miso_pf2_g8_sportapp.R
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentAddRoutsBinding
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentEditRoutsBinding
import com.miso202402.front_miso_pf2_g8_sportapp.src.services.ApiService
import com.miso202402.front_miso_pf2_g8_sportapp.src.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class
EditRoutsFragment : Fragment() {
    private var _binding: FragmentEditRoutsBinding? = null
    private lateinit var preferences: SharedPreferences
    private val binding get() = _binding!!
    private var domain: String = "https://g7o4mxf762.execute-api.us-east-1.amazonaws.com/prod/"
    private var vectorTipoDeporte  =  arrayOf("Atletismo", "Ciclismo")
    private var tipoDeporte : String? = null
    private lateinit var rout: Routs
    private lateinit var route_id: String;
    private lateinit var user_id: String
    private var route_date : String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        preferences = SharedPreferences(requireContext())
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditRoutsBinding.inflate(inflater, container, false)
        route_id = arguments?.getString("rout_id").toString()
        Log.i("rout_id", route_id)
        user_id = preferences.getData<String>("id").toString()
        Log.i("user_id", user_id)
        rout = Routs("", "","","","",
            "", "", "", "","","")
        getRoutById(route_id)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       /* var spinner = view.findViewById<Spinner>(R.id.spinner_EditRoutsFragment)
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
        }*/
        binding.buttonAgregarEditRoutsFragment.setOnClickListener {
            createTrainigSession(route_id, user_id)

        }
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

    private fun getRoutById(event_id: String){
        val utils = Utils()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val callGetRutaById = utils.getRetrofit(domain)
                    .create(ApiService::class.java)
                    .getRutaById(event_id)
                    .execute()
                val getRuta = callGetRutaById.body() as GetRoutsResponse?
                Log.i("Sali se la peticion getRuta", "Rest")
                Log.i("Sali a la peticion code ", getRuta?.code.toString())
                lifecycleScope.launch {
                    if (getRuta?.code == 200) {
                        rout = getRuta.content!!
                        binding.editTexNameEditRoutsFragment.setText(rout.route_name.toString())
                        binding.editTexDescriptionEditRoutsFragment.setText(rout.route_description.toString())
                        binding.editTexLocationIEditRoutsFragment.setText(rout.route_location_A.toString())
                        binding.editTexLocationFEditRoutsFragment.setText(rout.route_location_B.toString())
                        binding.editLinkEditRoutsFragment.setText(rout.link.toString())
                        binding.editTexDateEditRoutsFragment.setText(rout.route_date.toString())
                        route_date = rout.route_date.toString()
                      /* if (rout.sport == "Atletismo") {
                           binding.spinnerEditRoutsFragment.setSelection(0)
                           tipoDeporte = "Atletismo"

                       } else {
                           binding.spinnerEditRoutsFragment.setSelection(1)
                           tipoDeporte = "Ciclismo"

                       }*/
                    } else {
                        activity?.let {
                            utils.showMessageDialog(
                                it,
                                "Error Al traer la ruta, intente mas tarde"
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("error",e.message.toString())
            }
        }
    }

    private fun createTrainigSession(route_id: String, user_id: String){
        val utils = Utils()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.i("Entre", "update")
                Log.i("createTrainigSession","$user_id $route_id $tipoDeporte $route_date")
                val callCreateTrainigSession = utils.getRetrofit(domain)
                    .create(ApiService::class.java)
                    .createTrainigSession(
                        TrainingSessionRequest(
                            user_id,
                            route_id,
                            "ruta",
                            tipoDeporte,
                            //route_date.toString().replace("T"," ")
                            "2024-05-28T14:30:00"
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
                        findNavController().navigate(R.id.action_EditRoutsFragment_to_ListRoutsFragment)
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
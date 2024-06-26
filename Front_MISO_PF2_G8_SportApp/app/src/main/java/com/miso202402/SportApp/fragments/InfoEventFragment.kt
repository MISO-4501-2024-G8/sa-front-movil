package com.miso202402.SportApp.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.miso202402.SportApp.src.models.models.Events
import com.miso202402.SportApp.src.models.response.GetEventResponse
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
    lateinit var typePlan : String

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
        typePlan = preferences.getData<String>("typePlan").toString()
        Log.i("user_id", user_id)
        event = Events("", "","","","", "","", "")
        getEventById(event_id)
        binding.buttonAtrasEventsFragment.setOnClickListener(){
            val mainActivity = requireActivity() as? MainActivity
            mainActivity?.navigateToFragment(R.id.CalendarFragment, "Calendario")
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
                        binding.sportEventsFragment.setText(event.sport.toString())
                        binding.dateEventsFragment.setText(event.event_date.toString())
                        event_date = event.event_date.toString()
                        binding.buttonLinkEventsFragment.setOnClickListener{
                            val url = event.link.toString()
                            Log.i("URL LINK ",url)
                            if(url == "") {
                                mostrarSnackbar("No hay un link asociado, intente mas tarde")
                            }else{
                                Log.i("URL TO OPEN ",url)
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                startActivity(intent)
                            }
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


}
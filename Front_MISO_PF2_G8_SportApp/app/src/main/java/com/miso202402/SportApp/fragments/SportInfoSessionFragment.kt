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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.miso202402.SportApp.src.models.models.SportObjectiveSession
import com.miso202402.SportApp.src.models.models.SportSession
import com.miso202402.SportApp.src.models.response.GetEventResponse
import com.miso202402.SportApp.src.models.response.GetSportSessionResponse
import com.miso202402.SportApp.src.utils.ClickListener_SportObjectiveSession
import com.miso202402.SportApp.src.utils.SharedPreferences
import com.miso202402.SportApp.src.utils.SportObjectSessionAdapter
import com.miso202402.SportApp.src.utils.SportSessionAdapter
import com.miso202402.front_miso_pf2_g8_sportapp.R
import com.miso202402.front_miso_pf2_g8_sportapp.activities.MainActivity
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentSportInfoSessionBinding
import com.miso202402.front_miso_pf2_g8_sportapp.src.services.ApiService
import com.miso202402.front_miso_pf2_g8_sportapp.src.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SportInfoSessionFragment : Fragment(), ClickListener_SportObjectiveSession {
    private var _binding: FragmentSportInfoSessionBinding ? = null
    private val binding get() = _binding!!
    private lateinit var preferences: SharedPreferences
    private var domain: String = "https://g7o4mxf762.execute-api.us-east-1.amazonaws.com/prod/"
    private lateinit var sport_session_id: String;
    private lateinit var training_name: String;
    private lateinit var user_id: String;
    lateinit var typePlan : String
    private lateinit var sportSession:SportSession
    lateinit var listener:ClickListener_SportObjectiveSession

    override fun onAttach(context: Context) {
        super.onAttach(context)
        preferences = SharedPreferences(requireContext())
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSportInfoSessionBinding.inflate(inflater, container, false)
        sport_session_id = arguments?.getString("sport_session_id").toString()
        training_name = arguments?.getString("training_name").toString()
        user_id = preferences.getData<String>("id").toString()
        typePlan = preferences.getData<String>("typePlan").toString()
        Log.i("user_id", user_id)
        Log.i("sport_session_id", sport_session_id)
        Log.i("typePlan", typePlan)
        Log.i("training_name", training_name)
        listener = this
        //obtener el sportsesison por el id
        getSportSessionById(sport_session_id)
        binding.buttonAtras.setOnClickListener(){
            val mainActivity = requireActivity() as? MainActivity
            mainActivity?.navigateToFragment(R.id.SportFragment, "Sesion Deportiva")
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

    private fun getSportSessionById(sport_session_id: String){
        val utils = Utils()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val callGetSportSession = utils.getRetrofit(domain)
                    .create(ApiService::class.java)
                    .getSportSessionById(sport_session_id)
                    .execute()
                val getSportSession = callGetSportSession.body()as GetSportSessionResponse?
                Log.i("Sali se la peticion getSportSession", "Rest")
                Log.i("Sali a la peticion code ", getSportSession?.code.toString())
                lifecycleScope.launch {
                    if (getSportSession?.code == 200) {
                        sportSession = getSportSession.content!!
                        binding.tvPlanDesc.text = training_name
                        binding.titleSemana.text = "Semana: " + sportSession.week.toString()
                        binding.tvDiaDesc.text = "Dia: " + sportSession.day
                        binding.tvTiempoDesc.text = sportSession.total_time.toString() + "m"
                        binding.tvUbicacionDesc.text = sportSession.location
                        binding.tvFechaDesc.text = sportSession.session_event
                        binding.recyclerviewListObjectives
                        binding.recyclerviewListObjectives.setHasFixedSize(true)
                        binding.recyclerviewListObjectives.layoutManager =
                            LinearLayoutManager(context)
                        binding.recyclerviewListObjectives.adapter =
                            sportSession.objective_instructions?.let {
                                SportObjectSessionAdapter(
                                    it,
                                    listener
                                )
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

    override fun onCListItemClick(view: View, sportObjectiveSession: SportObjectiveSession){
        Log.i("sportObjectiveSession Item: ", sportObjectiveSession.id.toString())
    }

}
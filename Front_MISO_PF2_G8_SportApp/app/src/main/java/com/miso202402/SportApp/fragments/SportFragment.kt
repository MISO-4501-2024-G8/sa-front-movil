package com.miso202402.SportApp.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.miso202402.SportApp.src.models.models.SportSession
import com.miso202402.SportApp.src.models.models.TrainingPlan
import com.miso202402.SportApp.src.models.response.GetAllSportSessionResponse
import com.miso202402.SportApp.src.models.response.TrainingListPlansResponse
import com.miso202402.SportApp.src.utils.ClickListener_SportSession
import com.miso202402.SportApp.src.utils.SharedPreferences
import com.miso202402.SportApp.src.utils.SportSessionAdapter
import com.miso202402.SportApp.src.utils.TrainingPlanAdapter
import com.miso202402.front_miso_pf2_g8_sportapp.R
import com.miso202402.front_miso_pf2_g8_sportapp.activities.MainActivity
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentSportBinding
import com.miso202402.front_miso_pf2_g8_sportapp.src.services.ApiService
import com.miso202402.front_miso_pf2_g8_sportapp.src.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SportFragment : Fragment(), ClickListener_SportSession {

    private var _binding: FragmentSportBinding? = null
    private val binding get() = _binding!!
    private var user_id: String? = ""
    private var typePlan: String? = ""
    private lateinit var preferences: SharedPreferences
    private lateinit var sportSessionList : List<SportSession>
    private var domain: String = "https://g7o4mxf762.execute-api.us-east-1.amazonaws.com/prod/"
    lateinit var listener:ClickListener_SportSession
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
        _binding = FragmentSportBinding.inflate(inflater, container, false)
        user_id = preferences.getData<String>("id").toString()
        typePlan = preferences.getData<String>("typePlan").toString()
        Log.i("user_id", user_id!!)
        Log.i("typePlan", typePlan!!)
        sportSessionList = listOf()
        listener = this
        // llamar a traer todas las SportSessions
        getAllSportSession(user_id!!)
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
        binding.BtnIniciar.setOnClickListener {
            view?.let {
                Snackbar.make(it, "Funcionalidad en construccion...", Snackbar.LENGTH_SHORT).show()
            }
            /*
            val mainActivity = requireActivity() as? MainActivity
            mainActivity?.navigateToFragment(R.id.addTrainingPlanFragment, "Nuevo Plan")
            */
        }
    }

    private fun mostrarSnackbar(mensaje: String) {
        view?.let {
            Snackbar.make(it, mensaje, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun getAllSportSession(user_id:String){
        val utils = Utils()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val callGetAllSportSessions = utils.getRetrofit(domain)
                    .create(ApiService::class.java)
                    .getSportUserSessions(user_id)
                    .execute()
                val getAllSportSessionResponse = callGetAllSportSessions.body() as GetAllSportSessionResponse?
                if (getAllSportSessionResponse?.code == 200){
                    Log.i("callGetAllSportSessions","Antes de refrescar la lista")
                    sportSessionList = getAllSportSessionResponse?.content!!
                    if(_binding != null) {
                        withContext(Dispatchers.Main) {
                            binding.RVSportSession.setHasFixedSize(true)
                            binding.RVSportSession.layoutManager =
                                LinearLayoutManager(context)
                            binding.RVSportSession.adapter =
                                SportSessionAdapter(sportSessionList, listener)
                        }
                    }
                }else{
                    Log.e("getAllSportSessionResponse error: ",getAllSportSessionResponse?.message.toString())
                }
            } catch (e: Exception) {
                Log.e("error",e.message.toString())
            }
        }
    }

    override fun onCListItemClick(view: View, sportSession: SportSession){
        Log.i("sportSession Item: ", sportSession.id.toString())
        val bundle = bundleOf("sport_session_id" to sportSession.id )
        view?.let {
            Snackbar.make(it, "Funcionalidad en construccion...", Snackbar.LENGTH_SHORT).show()
        }
        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.navigateToFragment(R.id.SportInfoSessionFragment, "Detalle Sesion Deportiva",bundle)
    }


}
package com.miso202402.SportApp.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.miso202402.SportApp.src.models.models.RestRoutine
import com.miso202402.SportApp.src.models.models.TrainingPlan
import com.miso202402.SportApp.src.models.response.GetAllRestRoutineResponse
import com.miso202402.SportApp.src.utils.ClickListener_restroutines
import com.miso202402.SportApp.src.utils.RestRoutineAdapter
import com.miso202402.SportApp.src.utils.SharedPreferences
import com.miso202402.front_miso_pf2_g8_sportapp.R
import com.miso202402.front_miso_pf2_g8_sportapp.activities.MainActivity
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentListRestRoutineBinding
import com.miso202402.front_miso_pf2_g8_sportapp.src.services.ApiService
import com.miso202402.front_miso_pf2_g8_sportapp.src.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ListRestRoutineFragment : Fragment(), ClickListener_restroutines {

    private var _binding:FragmentListRestRoutineBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferences: SharedPreferences
    private var domain: String = "http://lb-ms-py-training-mngr-157212315.us-east-1.elb.amazonaws.com/"
    private lateinit var user_id: String;
    private lateinit var tempTrainingPlan: TrainingPlan
    private lateinit var restRoutinesList: List<RestRoutine>
    lateinit var listener: ClickListener_restroutines
    lateinit var btnSeleccionar: Button
    lateinit var btnAtras: Button
    private var rest_routine_id: String = "";
    private var typePlan: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        preferences = SharedPreferences(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListRestRoutineBinding.inflate(inflater, container, false)
        restRoutinesList = listOf()
        user_id = preferences.getData<String>("id").toString()
        Log.i("user_id", user_id)
        typePlan = preferences.getData<String>("typePlan").toString()
        tempTrainingPlan = preferences.getData<TrainingPlan>("tempTrainingPlan")!!
        if(tempTrainingPlan != null){
            rest_routine_id = tempTrainingPlan.id_rest_routine.toString()
        }
        listener = this
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
        btnSeleccionar = binding.buttonSeleccionarRestRoutinesFragment
        btnAtras = binding.buttonAtrasRestRoutinesFragment

        btnSeleccionar.setOnClickListener(){
            if(rest_routine_id == ""){
                mostrarSnackbar("Se debe seleccionar una rutina de descanso")
                return@setOnClickListener
            }
            tempTrainingPlan.id_rest_routine = rest_routine_id
            preferences.saveData("tempTrainingPlan", tempTrainingPlan)
            val mainActivity = requireActivity() as? MainActivity
            mainActivity?.navigateToFragment(R.id.addTrainingPlanFragment, "Nuevo Plan", null, typePlan)
        }
        btnAtras.setOnClickListener(){
            val mainActivity = requireActivity() as? MainActivity
            mainActivity?.navigateToFragment(R.id.addTrainingPlanFragment, "Nuevo Plan",null, typePlan)
        }
    }

    private fun mostrarSnackbar(mensaje: String) {
        view?.let {
            Snackbar.make(it, mensaje, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun getAllFoodRoutines(){
        val utils = Utils()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val callGetAllRestRoutine = utils.getRetrofit(domain)
                    .create(ApiService::class.java)
                    .getAllRestRoutineResponse()
                    .execute()
                val getAllRestRoutineResponse = callGetAllRestRoutine.body() as GetAllRestRoutineResponse?
                if (getAllRestRoutineResponse?.code == 200){
                    Log.i("getAllRestRoutineResponse","Antes de refrescar la lista")
                    restRoutinesList = getAllRestRoutineResponse?.rest_routines!!

                    if(_binding != null) {
                        withContext(Dispatchers.Main) {
                            binding.recyclerviewRestRoutinesFragment.setHasFixedSize(true)
                            binding.recyclerviewRestRoutinesFragment.layoutManager =
                                LinearLayoutManager(context)
                            binding.recyclerviewRestRoutinesFragment.adapter =
                                RestRoutineAdapter(restRoutinesList, listener)
                        }
                    }
                }else{
                    Log.e("getAllRestRoutineResponse error: ",getAllRestRoutineResponse?.message.toString())
                }
            } catch (e: Exception) {
                Log.e("error",e.message.toString())
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.showToolbarAndFab()
        getAllFoodRoutines()
    }

    override fun onListItemClick(view: View, restRoutine: RestRoutine, position: Int) {
        var realPosition: Int = position + 1
        rest_routine_id = restRoutine.id.toString()
        mostrarSnackbar("Rutina $realPosition Seleccionada")
    }

    override fun onListItemLongClick(view: View, restRoutine: RestRoutine, position: Int) {
        //mostrarSnackbar("onListItemLongClick En Construccion")
        var id_rest_routine = restRoutine.id.toString()
        val bundle = bundleOf(
            "id_rest_routine" to id_rest_routine
        )
        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.navigateToFragment(R.id.InfoRestRoutineFragment, "Detalle Rutina Descanso",bundle, typePlan)
    }


}
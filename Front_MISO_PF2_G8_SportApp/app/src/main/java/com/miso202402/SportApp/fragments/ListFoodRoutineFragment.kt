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
import com.miso202402.SportApp.src.models.models.Events
import com.miso202402.SportApp.src.models.models.FoodRoutine
import com.miso202402.SportApp.src.models.models.TrainingPlan
import com.miso202402.SportApp.src.models.response.GetAllEatingRoutineResponse
import com.miso202402.SportApp.src.models.response.TrainingListPlansResponse
import com.miso202402.SportApp.src.utils.ClicTSListener
import com.miso202402.SportApp.src.utils.ClickListener_foodroutines
import com.miso202402.SportApp.src.utils.FoodRoutineAdapter
import com.miso202402.SportApp.src.utils.SharedPreferences
import com.miso202402.SportApp.src.utils.TrainingPlanAdapter
import com.miso202402.front_miso_pf2_g8_sportapp.R
import com.miso202402.front_miso_pf2_g8_sportapp.activities.MainActivity
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentListFoodRoutineBinding
import com.miso202402.front_miso_pf2_g8_sportapp.src.services.ApiService
import com.miso202402.front_miso_pf2_g8_sportapp.src.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListFoodRoutineFragment : Fragment(), ClickListener_foodroutines {
    private var _binding:FragmentListFoodRoutineBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferences: SharedPreferences
    private var domain: String = "http://lb-ms-py-training-mngr-157212315.us-east-1.elb.amazonaws.com/"
    private lateinit var user_id: String;
    private lateinit var tempTrainingPlan: TrainingPlan
    private lateinit var foodRoutines: List<FoodRoutine>
    lateinit var listener: ClickListener_foodroutines
    lateinit var btnSeleccionar: Button
    lateinit var btnAtras: Button
    private var food_routine_id: String = "";

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
        _binding = FragmentListFoodRoutineBinding.inflate(inflater, container, false)
        foodRoutines = listOf()
        user_id = preferences.getData<String>("id").toString()
        Log.i("user_id", user_id)
        tempTrainingPlan = preferences.getData<TrainingPlan>("tempTrainingPlan")!!
        if(tempTrainingPlan != null){
            food_routine_id = tempTrainingPlan.id_eating_routine.toString()
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
        btnSeleccionar = binding.buttonSeleccionarFoodRoutinesFragment
        btnAtras = binding.buttonAtrasFoodRoutinesFragment

        btnSeleccionar.setOnClickListener(){
            if(food_routine_id == ""){
                mostrarSnackbar("Se debe seleccionar una rutina de alimentacion")
                return@setOnClickListener
            }
            tempTrainingPlan.id_eating_routine = food_routine_id
            preferences.saveData("tempTrainingPlan", tempTrainingPlan)
            val mainActivity = requireActivity() as? MainActivity
            mainActivity?.navigateToFragment(R.id.addTrainingPlanFragment, "Nuevo Plan")
        }
        btnAtras.setOnClickListener(){
            val mainActivity = requireActivity() as? MainActivity
            mainActivity?.navigateToFragment(R.id.addTrainingPlanFragment, "Nuevo Plan")
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
                val callGetAllEatingRoutine = utils.getRetrofit(domain)
                    .create(ApiService::class.java)
                    .getAllEatingRoutines()
                    .execute()
                val getAllEatingRoutineResponse = callGetAllEatingRoutine.body() as GetAllEatingRoutineResponse?
                if (getAllEatingRoutineResponse?.code == 200){
                    Log.i("getAllEatingRoutineResponse","Antes de refrescar la lista")
                    foodRoutines = getAllEatingRoutineResponse?.eating_routine!!

                    if(_binding != null) {
                        withContext(Dispatchers.Main) {
                            binding.recyclerviewFoodRoutinesFragment.setHasFixedSize(true)
                            binding.recyclerviewFoodRoutinesFragment.layoutManager =
                                LinearLayoutManager(context)
                            binding.recyclerviewFoodRoutinesFragment.adapter =
                                FoodRoutineAdapter(foodRoutines, listener)
                        }
                    }
                }else{
                    Log.e("getAllEatingRoutineResponse error: ",getAllEatingRoutineResponse?.message.toString())
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

    override fun onListItemClick(view: View, foodRoutine: FoodRoutine, position: Int) {
        //mostrarSnackbar("onListItemClick En Construccion")
        var realPosition: Int = position + 1
        food_routine_id = foodRoutine.id.toString()
        mostrarSnackbar("Rutina $realPosition Seleccionada")
    }

    override fun onListItemLongClick(view: View, foodRoutine: FoodRoutine, position: Int) {
        //mostrarSnackbar("onListItemLongClick En Construccion")
        var id_food_routine = foodRoutine.id.toString()
        val bundle = bundleOf(
            "id_food_routine" to id_food_routine
        )
        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.navigateToFragment(R.id.InfoFoodRoutineFragment, "Detalle Rutina Alimentacion",bundle)
    }
}
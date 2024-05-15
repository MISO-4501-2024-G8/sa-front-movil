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
import androidx.annotation.RequiresExtension
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.miso202402.SportApp.src.models.models.FoodObjective
import com.miso202402.SportApp.src.models.models.Objective
import com.miso202402.SportApp.src.models.response.GetFoodRoutineResponse
import com.miso202402.SportApp.src.models.response.GetRestRoutineResponse
import com.miso202402.SportApp.src.utils.FoodRoutineDetailAdapter
import com.miso202402.SportApp.src.utils.RestDetailAdapter
import com.miso202402.SportApp.src.utils.SharedPreferences
import com.miso202402.front_miso_pf2_g8_sportapp.R
import com.miso202402.front_miso_pf2_g8_sportapp.activities.MainActivity
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentInfoFoodRoutineBinding
import com.miso202402.front_miso_pf2_g8_sportapp.src.services.ApiService
import com.miso202402.front_miso_pf2_g8_sportapp.src.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InfoFoodRoutineFragment : Fragment() {
    private var _binding:FragmentInfoFoodRoutineBinding? = null
    private val binding get() = _binding!!
    private lateinit var listFoodObjectives: MutableList<FoodObjective>
    private var domain : String = "http://lb-ms-py-training-mngr-157212315.us-east-1.elb.amazonaws.com/"
    private lateinit var id_training_plan: String;
    private lateinit var id_food_routine: String;
    private lateinit var preferences: SharedPreferences
    private var typePlan: String? = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        preferences = SharedPreferences(requireContext())
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInfoFoodRoutineBinding.inflate(inflater, container, false)
        listFoodObjectives = mutableListOf()
        id_training_plan = arguments?.getString("training_plan_id").toString()
        id_food_routine = arguments?.getString("id_food_routine").toString()
        Log.i("id_training_plan", id_training_plan)
        Log.i("id_food_routine", id_food_routine)
        typePlan = preferences.getData<String>("typePlan").toString()
        getFoodRoutineDetailById(id_food_routine)
        binding.recyclerviewListObjectives .setHasFixedSize(true)
        binding.recyclerviewListObjectives.layoutManager = LinearLayoutManager(context)
        binding.recyclerviewListObjectives.adapter = FoodRoutineDetailAdapter(listFoodObjectives)
        binding.buttonAtras.setOnClickListener(){
            // revisar si id_training_plan es diferente de null o vacio devolver a info de training plan de lo contrario a lista de rest services
            if(id_training_plan != null && id_training_plan != "null" && id_training_plan  != "" ){
                val bundle = bundleOf("training_plan_id" to id_training_plan )
                val mainActivity = requireActivity() as? MainActivity
                mainActivity?.navigateToFragment(R.id.InfoTrainingPlanFragment, "Detalle Plan de Entrenamiento",bundle)
            }else {
                val mainActivity = requireActivity() as? MainActivity
                mainActivity?.navigateToFragment(R.id.FoodRoutineListFragment, "Rutina de Alimentacion")
            }
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

    private fun getFoodRoutineDetailById(id_food_routine: String){
        val utils = Utils()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val callGetFoodRoutineById = utils.getRetrofit(domain)
                    .create(ApiService::class.java)
                    .getFoodRoutine(id_food_routine)
                    .execute()
                val getFoodRoutine = callGetFoodRoutineById.body() as GetFoodRoutineResponse?
                Log.i("getFoodRoutine", getFoodRoutine?.code.toString())
                lifecycleScope.launch {
                    if (getFoodRoutine?.code == 200) {
                        var content = getFoodRoutine.eating_routine!!
                        binding.nameFRoutine.text = "Nombre: " + content.name
                        binding.descFRoutine.text = "Descripcion: " + content.description

                        if(typePlan != "basico"){
                            binding.buttonFoodServices.visibility = View.VISIBLE
                            binding.buttonFoodServices.setOnClickListener(){
                                mostrarSnackbar("En Construccion")
                            }
                        }
                        val daysOfWeek = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "lunes", "martes", "miercoles", "jueves", "viernes")
                        for (day in daysOfWeek) {
                            val listDay = content.day_food_plans?.filter {
                                it.day == day
                            }
                            if (listDay != null && listDay.isNotEmpty()) {
                                val foodObjective = FoodObjective(day, listDay)
                                listFoodObjectives.add(foodObjective)
                            }
                        }

                        var listObjectives = listFoodObjectives?.toList() ?: listOf()
                        val filteredObjectives = listObjectives
                            .filter { it.day in listOf("Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "lunes", "martes", "miercoles", "jueves", "viernes") } // Filtrar por los días deseados
                            .sortedBy { when(it.day) { // Ordenar los objetivos según el orden de los días
                                "Lunes" -> 1
                                "Martes" -> 2
                                "Miercoles" -> 3
                                "Jueves" -> 4
                                "Viernes" -> 5
                                "lunes" -> 1
                                "martes" -> 2
                                "miercoles" -> 3
                                "jueves" -> 4
                                "viernes" -> 5
                                else -> 6
                            }}
                        binding.recyclerviewListObjectives.setHasFixedSize(true)
                        binding.recyclerviewListObjectives.layoutManager = LinearLayoutManager(context)
                        binding.recyclerviewListObjectives.adapter = FoodRoutineDetailAdapter(filteredObjectives)
                    } else {
                        activity?.let {
                            utils.showMessageDialog(
                                it,
                                "Error Al traer el rutina de descanso, intente mas tarde"
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
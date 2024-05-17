package com.miso202402.SportApp.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.RequiresExtension
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.miso202402.SportApp.src.models.models.TrainingPlan
import com.miso202402.SportApp.src.models.response.TrainingListPlansResponse
import com.miso202402.SportApp.src.utils.ClicTPListener
import com.miso202402.SportApp.src.utils.PreferenceHelper
import com.miso202402.SportApp.src.utils.SharedPreferences
import com.miso202402.SportApp.src.utils.TrainingPlanAdapter
import com.miso202402.front_miso_pf2_g8_sportapp.R
import com.miso202402.front_miso_pf2_g8_sportapp.R.*
import com.miso202402.front_miso_pf2_g8_sportapp.activities.MainActivity
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentTrainingSessionBinding
import com.miso202402.front_miso_pf2_g8_sportapp.src.services.ApiService
import com.miso202402.front_miso_pf2_g8_sportapp.src.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TrainingSessionFragment : Fragment(), ClicTPListener {


    private var _binding: FragmentTrainingSessionBinding? = null
    private val binding get() = _binding!!
    private var tipoDeporte : String? = null
    private var vectorTipoDeporte  =  arrayOf("Todos","Atletismo", "Ciclismo")
    private var user_id: String? = ""
    private var typePlan: String? = ""
    private lateinit var preferences: SharedPreferences
    private lateinit var trainingPlanList : List<TrainingPlan>
    private lateinit var trainingPlanListFilter : List<TrainingPlan>
    //private var domain: String = "https://g7o4mxf762.execute-api.us-east-1.amazonaws.com/prod/"
    private var domain: String = "http://lb-ms-py-training-mngr-157212315.us-east-1.elb.amazonaws.com/"
    lateinit var listener: ClicTPListener



    override fun onAttach(context: Context) {
        super.onAttach(context)
        preferences = SharedPreferences(requireContext())
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrainingSessionBinding.inflate(inflater, container, false)
        user_id = preferences.getData<String>("id").toString()
        typePlan = preferences.getData<String>("typePlan").toString()
        Log.i("user_id", user_id!!)
        Log.i("typePlan", typePlan!!)
        trainingPlanList = listOf()
        trainingPlanListFilter = listOf()
        listener = this
        //Llamar a lista de planes
        getAllTrainingPlans()
        preferences.clearData("tempTrainingPlan")
        preferences.clearData("alertasE")
        context?.let { PreferenceHelper.clearTrainingPlan(it) }
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

        var spinner = view.findViewById<Spinner>(R.id.spinner_TrainingSessionFragment)
        activity?.let {
            ArrayAdapter.createFromResource(it,
                                            array.Sport,
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
                trainingPlanListFilter = trainingPlanList
                var datosFiltrados: List<TrainingPlan> = trainingPlanListFilter
                if(tipoDeporte !== "Todos") {
                    datosFiltrados = trainingPlanListFilter.filter { item ->
                        item.sport == tipoDeporte
                    }
                }
                trainingPlanListFilter = datosFiltrados
                binding.recyclerviewTrainingSessionFragment.setHasFixedSize(true)
                binding.recyclerviewTrainingSessionFragment.layoutManager =
                    LinearLayoutManager(context)
                binding.recyclerviewTrainingSessionFragment.adapter =
                    TrainingPlanAdapter(trainingPlanListFilter, listener)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
        binding.buttonAgregarFragmentTrainingSession.setOnClickListener {
            val mainActivity = requireActivity() as? MainActivity
            mainActivity?.navigateToFragment(R.id.addTrainingPlanFragment, "Nuevo Plan", null)
        }
    }

    private fun mostrarSnackbar(mensaje: String) {
        view?.let {
            Snackbar.make(it, mensaje, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun getAllTrainingPlans(){
        val utils = Utils()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val callGetTrainingPlans = utils.getRetrofit(domain)
                    .create(ApiService::class.java)
                    .getTrainingPlans()
                    .execute()
                val getAllTrainingListPlansResponse = callGetTrainingPlans.body() as TrainingListPlansResponse?
                if (getAllTrainingListPlansResponse?.code == 200){
                    Log.i("callGetTrainingPlans","Antes de refrescar la lista")
                    trainingPlanList = getAllTrainingListPlansResponse?.training_plans!!
                    trainingPlanListFilter = trainingPlanList
                    if(_binding != null) {
                        withContext(Dispatchers.Main) {
                            binding.recyclerviewTrainingSessionFragment.setHasFixedSize(true)
                            binding.recyclerviewTrainingSessionFragment.layoutManager =
                                LinearLayoutManager(context)
                            binding.recyclerviewTrainingSessionFragment.adapter =
                                TrainingPlanAdapter(trainingPlanListFilter, listener)
                        }
                    }
                }else{
                    Log.e("getAllTrainingPlans error: ",getAllTrainingListPlansResponse?.message.toString())
                }
            } catch (e: Exception) {
                Log.e("error",e.message.toString())
            }
        }
    }

    /*
    override fun onResume() {
        super.onResume()
        getAllTrainingPlans()
    }
    */

    private fun CreateTrainingSesion(){

    }

    override fun onCListItemClick(view: View, trainingPlan: TrainingPlan) {
        Log.i("TPlan Item: ", trainingPlan.name.toString())
        val bundle = bundleOf("training_plan_id" to trainingPlan.id )
        /*
        view?.let {
            Snackbar.make(it, "Funcionalidad en construccion...", Snackbar.LENGTH_SHORT).show()
        }*/
        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.navigateToFragment(R.id.InfoTrainingPlanFragment, "Detalle Plan de Entrenamiento",bundle)
    }

}
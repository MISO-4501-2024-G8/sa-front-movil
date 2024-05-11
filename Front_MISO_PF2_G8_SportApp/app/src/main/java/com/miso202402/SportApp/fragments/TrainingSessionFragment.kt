package com.miso202402.SportApp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.miso202402.SportApp.src.utils.WeeksAdapter
import com.miso202402.front_miso_pf2_g8_sportapp.R
import com.miso202402.front_miso_pf2_g8_sportapp.R.*
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentTrainingSessionBinding


class TrainingSessionFragment : Fragment() {


    private var _binding: FragmentTrainingSessionBinding? = null
    private val binding get() = _binding!!

    private var tipoDeporte : String? = null
    private var vectorTipoDeporte  =  arrayOf("Atletismo", "Ciclismo")
    private var token: String? = ""
    private var id: String? = ""




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrainingSessionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val weeksAdapter =  WeeksAdapter(this.vectorTipoDeporte)
        //val recyclerView: RecyclerView = view.findViewById(R.id.recyclerview_TrainingSessionFragment)
        //recyclerView.layoutManager = LinearLayoutManager(view.context)
        //recyclerView.adapter = weeksAdapter

        arguments?.getString("token")?.let {
            this.token = it.toString()
            Log.i("token", this.token.toString())
        }
        arguments?.getString("id")?.let {
            this.id = it.toString()
            Log.i("id", this.id.toString())
        }
        //token
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
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        binding.buttonAgregarFragmentTrainingSession.setOnClickListener {
            val bundle = bundleOf(
                "token" to  arguments?.getString("token"),
                "id" to  arguments?.getString("id"),
                "sport" to tipoDeporte
            )
            findNavController().navigate(
                R.id.action_trainingSessionFragment_to_addTrainingPlanFragment,
                bundle)
        }

       // val dataset = queryTrainingSesion()



    }



    /*private fun queryMyTrainngsPlan(){
        val loginRequest = LoginRequest(email, password)
        val utils = Utils()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                //val a = GetTrainingPlansResponse(null,null,null,null)
                val callLogin = utils.getRetrofit(domain).create(ApiService::class.java).getTrainingPlan("") .execute()
                val loginResponse = callLogin.body() as LoginResponse?
                lifecycleScope.launch {
                    if(errorTimesLoginRejected < 3 ) {
                        if (loginResponse?.message == "Usuario logueado correctamante") {
                            activity?.let { utils.showMessageDialog(it, loginResponse?.message.toString())}
                            Log.i("mesnaje al loguearse", loginResponse?.message.toString())
                            errorTimesLoginRejected = 0
                            val bundle = bundleOf("token" to  loginResponse?.token)
                            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment, bundle)
                        }
                        else {
                            errorTimesLoginRejected++
                            val errorToLogin: String = loginResponse?.error.toString()
                            activity?.let { utils.showMessageDialog(it, errorToLogin)}
                            Log.e("error al loguearse", errorToLogin)
                        }
                    } else{
                        val errorMesage: String = "Supero la cantidad de intentos de login"
                        //activity?.let { utils.showMessageDialog(it, errorMesage) }
                        activity?.let { showMessageDialog(it, errorMesage) }
                        Log.e("error al loguearse por cantodad de intentos", errorMesage)
                    }
                }

            } catch (e: Exception) {
                Log.e("error",e.message.toString())
            }
        }
    }*/
    private fun CreateTrainingSesion(){
        TODO("Not yet implemented")
    }



}
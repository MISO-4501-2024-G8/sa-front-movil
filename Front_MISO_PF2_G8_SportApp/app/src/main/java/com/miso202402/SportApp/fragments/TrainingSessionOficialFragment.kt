package com.miso202402.SportApp.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.RadioButton
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.miso202402.SportApp.src.models.models.ConsultationsSessions
import com.miso202402.SportApp.src.models.models.Doctors
import com.miso202402.SportApp.src.models.models.Trainers
import com.miso202402.SportApp.src.models.models.TrainingSession
import com.miso202402.SportApp.src.models.request.ConsultationRequest
import com.miso202402.SportApp.src.models.response.GetAllConsultationSessionsResponse
import com.miso202402.SportApp.src.models.response.GetAllDoctorsResponse
import com.miso202402.SportApp.src.models.response.GetAllTrainersResponse
import com.miso202402.SportApp.src.models.response.GetAllUserTrainingSessionsResponse
import com.miso202402.SportApp.src.models.response.GetConsultationByIdResponse
import com.miso202402.SportApp.src.utils.ClicListener_DoctorsTrainers
import com.miso202402.SportApp.src.utils.ClicListener_ProgramConsultation
import com.miso202402.SportApp.src.utils.DoctorsTrainersAdapter
import com.miso202402.SportApp.src.utils.ProgramConsultationsAdapter
import com.miso202402.SportApp.src.utils.SharedPreferences
import com.miso202402.front_miso_pf2_g8_sportapp.R
import com.miso202402.front_miso_pf2_g8_sportapp.activities.MainActivity
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentTrainingSessionBinding
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentTrainingSessionOficialBinding
import com.miso202402.front_miso_pf2_g8_sportapp.src.services.ApiService
import com.miso202402.front_miso_pf2_g8_sportapp.src.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TrainingSessionOficialFragment : Fragment(), ClicListener_DoctorsTrainers {

    private var _binding: FragmentTrainingSessionOficialBinding? = null
    private val binding get() = _binding!!

    private lateinit var preferences: SharedPreferences
    private var domain: String = "https://g7o4mxf762.execute-api.us-east-1.amazonaws.com/prod/"
    private lateinit var user_id: String;
    private var token: String? = ""
    private lateinit var doctorList : List<Doctors>
    private lateinit var entrenadorList : List<Trainers>
    private lateinit var  doctor : Doctors
    private lateinit var  entrenador : Trainers
    private lateinit var date: String
    lateinit var listener: ClicListener_DoctorsTrainers
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        doctorList = emptyList()
        entrenadorList = emptyList()
        doctor = Doctors("", "", "", "")
        entrenador = Trainers("", "", "")

        _binding = FragmentTrainingSessionOficialBinding.inflate(inflater, container, false)
        user_id = preferences.getData<String>("id").toString()
        listener = this

        binding.radioButtonVirtualSessionTrainingSessionOficialFragment.setOnClickListener{
            if(binding.radioButtonLocalSessionTrainingSessionOficialFragment.isChecked){
                binding.radioButtonLocalSessionTrainingSessionOficialFragment.isChecked = false
            }
        }
        binding.radioButtonLocalSessionTrainingSessionOficialFragment.setOnClickListener{
            if(binding.radioButtonVirtualSessionTrainingSessionOficialFragment.isChecked){
                binding.radioButtonVirtualSessionTrainingSessionOficialFragment.isChecked = false
            }
        }

        binding.radioButtonDeportologoTrainingSessionOficialFragment.setOnClickListener{
            if(binding.radioButtonEntrenadorTrainingSessionOficialFragment.isChecked){
                binding.radioButtonEntrenadorTrainingSessionOficialFragment.isChecked = false
            }
            fetchGetAllDoctors()
        }
        binding.radioButtonEntrenadorTrainingSessionOficialFragment.setOnClickListener{
            if(binding.radioButtonDeportologoTrainingSessionOficialFragment.isChecked){
                binding.radioButtonDeportologoTrainingSessionOficialFragment.isChecked = false
            }
            fetchGetAllEntrenadores()
        }
        binding.buttonAgregarTrainingSessionOficialFragment.setOnClickListener {
           /*if (binding.radioButtonVirtualSessionTrainingSessionOficialFragment.isChecked ||
               binding.radioButtonLocalSessionTrainingSessionOficialFragment.isChecked ||
               binding.editTexDescriptionTrainingSessionOficialFragment.text.toString() == ""){
               lifecycleScope.launch {*/
                  // val util = Utils()
                   //val message = "Alguno de los campos estas vacios"
                  // util.showMessageDialog(context, message)
               //}
          // } else{
                date = binding.calendarViewTrainingSessionOficialFragment.year.toString() + "-" +
                        binding.calendarViewTrainingSessionOficialFragment.month + "-" +
                        binding.calendarViewTrainingSessionOficialFragment.dayOfMonth
                Log.i("Info date", date)

                CoroutineScope(Dispatchers.Main).launch {
                    createConsultation(date)
                }

            //}
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        preferences = SharedPreferences(requireContext())
    }


    private suspend fun createConsultation(date: String) {
        val utils = Utils()
        return withContext(Dispatchers.IO) {
            try {
                Log.i("user_id", user_id)
                Log.i("doctor_id", doctor.id.toString())
                Log.i("entrenador_id", entrenador.id.toString())
                val createConsultation = utils.getRetrofit(domain)
                    .create(ApiService::class.java)
                    .createConsultation(ConsultationRequest(
                        if(doctor != null) doctor.id  else entrenador.id,
                        user_id,
                        if(binding.radioButtonVirtualSessionTrainingSessionOficialFragment.isChecked) "Virtual" else "Presencial",
                        date,
                        ""

                    ))
                    .execute()
                val getAllConsultationByUserIdResponse = createConsultation.body() as GetConsultationByIdResponse?

                Log.i("getAllConsultationByUserIdResponse: ", getAllConsultationByUserIdResponse?.code.toString())
                Log.i("getAllConsultation Size: ",
                    getAllConsultationByUserIdResponse?.content?.id!!
                )
                if (getAllConsultationByUserIdResponse?.code == 201){

                    val message: String = "La session fue programada exitosamante"
                    activity?.let { utils.showMessageDialog(it, message)}
                    Log.e("Ok crear session depor", message)

                    val mainActivity = requireActivity() as? MainActivity
                    mainActivity?.navigateToFragment(R.id.ListProgramSessionsConsultationsFragment)

                } else {
                    val message: String = "La session no fue programada exitosamante"
                    activity?.let { utils.showMessageDialog(it, message)}
                    Log.e("Fallo al Crear la Sesion", message)

                }
            } catch (e: Exception) {
                Log.e("error", e.message.toString())
                //return@withContext emptyList()
            }
        }
    }

    private fun fetchGetAllDoctors() {
        progressBarVisible(true)
        CoroutineScope(Dispatchers.Main).launch {
            doctorList = getAllDocotros()
            Log.i("doctorList.size: ", doctorList.size.toString())
            binding.recyclerviewTrainingSessionOficialFragment.setHasFixedSize(true)
            binding.recyclerviewTrainingSessionOficialFragment.layoutManager = LinearLayoutManager(context)
            binding.recyclerviewTrainingSessionOficialFragment.adapter =
               DoctorsTrainersAdapter  (doctorList, emptyList(), listener)
            progressBarVisible(false)
        }
    }

    private suspend fun getAllDocotros(): List<Doctors> {
        val utils = Utils()
        return withContext(Dispatchers.IO) {
            try {
                val callGetAllDoctors = utils.getRetrofit(domain)
                    .create(ApiService::class.java)
                    .getAllDoctors()
                    .execute()
                val GetAllDoctors = callGetAllDoctors.body() as GetAllDoctorsResponse?
                Log.i("GetAllDoctors: ", GetAllDoctors?.code.toString())
                if (GetAllDoctors?.code == 200) {
                    return@withContext GetAllDoctors?.content ?: emptyList()
                } else {
                    return@withContext emptyList()
                }
            } catch (e: Exception) {
                Log.e("error", e.message.toString())
                return@withContext emptyList()
            }
        }
    }

    private fun fetchGetAllEntrenadores() {
        progressBarVisible(true)
        CoroutineScope(Dispatchers.Main).launch {
            entrenadorList = getAllEntrenadores()
            Log.i("entrenadorList.size: ", entrenadorList.size.toString())
            binding.recyclerviewTrainingSessionOficialFragment.setHasFixedSize(true)
            binding.recyclerviewTrainingSessionOficialFragment.layoutManager = LinearLayoutManager(context)
            binding.recyclerviewTrainingSessionOficialFragment.adapter =
                DoctorsTrainersAdapter (emptyList(), entrenadorList, listener)
            progressBarVisible(false)
        }
    }

    private suspend fun getAllEntrenadores(): List<Trainers> {
        val utils = Utils()
        return withContext(Dispatchers.IO) {
            try {
                val callGetAllEntrenadores = utils.getRetrofit(domain)
                    .create(ApiService::class.java)
                    .getAllTrainers()
                    .execute()
                val GetAllEntrenenadores = callGetAllEntrenadores.body() as GetAllTrainersResponse?
                Log.i("GetAllEntrenenadores: ", GetAllEntrenenadores?.code.toString())
                if (GetAllEntrenenadores?.code == 200) {
                    return@withContext GetAllEntrenenadores?.content ?: emptyList()
                } else {
                    return@withContext emptyList()
                }
            } catch (e: Exception) {
                Log.e("error", e.message.toString())
                return@withContext emptyList()
            }
        }
    }

    private fun progressBarVisible(valueV:Boolean){
        if(valueV){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onCListItemClick(view: View, doctor: Doctors?, trainer: Trainers?) {
       if (doctor != null){
           this.doctor = doctor!!
           Log.i("doctor", doctor.id.toString())
       }
        if(trainer != null){
            this.entrenador = trainer!!
            Log.i("entrenador", entrenador.id.toString())
        }
    }


}
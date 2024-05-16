package com.miso202402.SportApp.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.miso202402.SportApp.src.models.models.Doctors
import com.miso202402.SportApp.src.models.models.Trainers
import com.miso202402.SportApp.src.models.request.ConsultationRequest
import com.miso202402.SportApp.src.models.response.GetAllDoctorsResponse
import com.miso202402.SportApp.src.models.response.GetAllTrainersResponse
import com.miso202402.SportApp.src.models.response.GetConsultationByIdResponse
import com.miso202402.SportApp.src.utils.ClicListener_DoctorsTrainers
import com.miso202402.SportApp.src.utils.DoctorsTrainersAdapter
import com.miso202402.SportApp.src.utils.SharedPreferences
import com.miso202402.front_miso_pf2_g8_sportapp.R
import com.miso202402.front_miso_pf2_g8_sportapp.activities.MainActivity
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentTrainingSessionOficialBinding
import com.miso202402.front_miso_pf2_g8_sportapp.src.services.ApiService
import com.miso202402.front_miso_pf2_g8_sportapp.src.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar


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
    private lateinit var listener: ClicListener_DoctorsTrainers
    private lateinit var calendar : Calendar
    private var clic: Int = 0
    private lateinit var fecha :String
    private var typePlan: String? = ""

    @RequiresApi(Build.VERSION_CODES.O)
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
        typePlan = preferences.getData<String>("typePlan").toString()
        calendar = Calendar.getInstance()
        binding.calendarViewTrainingSessionOficialFragment.setMinDate(calendar.getTimeInMillis() +(24*60*60*1000));
        fecha = binding.calendarViewTrainingSessionOficialFragment.year.toString() + "-" +
                binding.calendarViewTrainingSessionOficialFragment.month + "-" +
                binding.calendarViewTrainingSessionOficialFragment.dayOfMonth


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
            //binding.textView2.visibility = View.VISIBLE
            fetchGetAllDoctors()
        }
        binding.radioButtonEntrenadorTrainingSessionOficialFragment.setOnClickListener{
            if(binding.radioButtonDeportologoTrainingSessionOficialFragment.isChecked){
                binding.radioButtonDeportologoTrainingSessionOficialFragment.isChecked = false
            }
            binding.textView2.visibility = View.VISIBLE
            fetchGetAllEntrenadores()
        }
        binding.calendarViewTrainingSessionOficialFragment.setOnDateChangedListener { datePicker, i, i2, i3 ->
           fecha = binding.calendarViewTrainingSessionOficialFragment.year.toString() + "-" +
                    binding.calendarViewTrainingSessionOficialFragment.month + "-" +
                    binding.calendarViewTrainingSessionOficialFragment.dayOfMonth
        }

        binding.buttonAgregarTrainingSessionOficialFragment.setOnClickListener {

          if ((binding.radioButtonVirtualSessionTrainingSessionOficialFragment.isChecked ||
               binding.radioButtonLocalSessionTrainingSessionOficialFragment.isChecked)
              &&
              (binding.radioButtonDeportologoTrainingSessionOficialFragment.isChecked ||
               binding.radioButtonEntrenadorTrainingSessionOficialFragment.isChecked )
              &&
              (clic == 1)){
              Log.i("is checked ",binding.radioButtonVirtualSessionTrainingSessionOficialFragment.isChecked.toString())
              Log.i("is checked2 ",binding.radioButtonLocalSessionTrainingSessionOficialFragment.isChecked.toString())
               date = fecha
               Log.i("Info date", date)
               CoroutineScope(Dispatchers.Main).launch {
                   createConsultation(date)
               }
           }
           else{
               val utils = Utils()
               lifecycleScope.launch {
                   val message: String = "Uno o multiples Campos no han sido seleccionados"
                   activity?.let { utils.showMessageDialog(it, message)}
               }

           }
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
                Log.i("radioButtonVirtualSession", binding.radioButtonVirtualSessionTrainingSessionOficialFragment.isChecked.toString())
                val tipoEvento = if(binding.radioButtonVirtualSessionTrainingSessionOficialFragment.isChecked) "Virtual" else "Presencial"
                Log.i("tipoEvento", tipoEvento)
                val idDeportolgo : String? =  if( doctor.id.toString() != "") doctor.id  else entrenador.id
                Log.i("id deportologo", idDeportolgo.toString())
                val createConsultation = utils.getRetrofit(domain)
                    .create(ApiService::class.java)
                    .createConsultation(ConsultationRequest(
                        idDeportolgo,
                        user_id,
                        tipoEvento,
                        date,
                        generateLinkOrDirection(tipoEvento)

                    ))
                    .execute()
                val getAllConsultationByUserIdResponse = createConsultation.body() as GetConsultationByIdResponse?

                Log.i("getAllConsultationByUserIdResponse: ", getAllConsultationByUserIdResponse?.code.toString())
                Log.i("getAllConsultation Size: ",
                    getAllConsultationByUserIdResponse?.content?.id!!
                )
                lifecycleScope.launch {
                    if (getAllConsultationByUserIdResponse?.code == 201){
                        val message: String = "La session fue programada exitosamante"
                        activity?.let { utils.showMessageDialog(it, message)}
                        Log.e("Ok crear session depor", message)
                        val mainActivity = requireActivity() as? MainActivity
                        mainActivity?.navigateToFragment(R.id.ListProgramSessionsConsultationsFragment,"", null, typePlan)
                    } else {
                        val message: String = "La session no fue programada exitosamante"
                        activity?.let { utils.showMessageDialog(it, message)}
                        Log.e("Fallo al Crear la Sesion", message)
                    }
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

    private fun generateLinkOrDirection(tipo:String): String {
        Log.i("tipo de evento Andres", tipo)
        val evento = if(tipo == "Virtual") "http://linkreu" + ".edu.co" else "Carrera 20 #-89-47 Bogota"
        Log.i("tipo de evento Eduardo ", evento)
        return evento
    }

    override fun onCListItemClick(view: View, doctor: Doctors?, trainer: Trainers?) {
        val utils = Utils()
        if (doctor != null){
            this.doctor = doctor!!
            Log.i("doctor", doctor.id.toString())
        }
        if(trainer != null){
            this.entrenador = trainer!!
            Log.i("entrenador", entrenador.id.toString())
        }
        this.clic = 1
        lifecycleScope.launch {

            val message: String = "Selecciono al entrenador deportologo "
            activity?.let { utils.showMessageDialog(it, message)}
        }
    }


}
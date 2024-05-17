package com.miso202402.SportApp.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.miso202402.SportApp.src.models.models.Doctors
import com.miso202402.SportApp.src.models.models.Trainers
import com.miso202402.SportApp.src.models.response.GetAllDoctorsResponse
import com.miso202402.SportApp.src.models.response.GetAllTrainersResponse
import com.miso202402.SportApp.src.utils.ClicListener_DoctorsTrainers
import com.miso202402.SportApp.src.utils.DoctorsTrainersAdapter
import com.miso202402.SportApp.src.utils.SharedPreferences
import com.miso202402.front_miso_pf2_g8_sportapp.R
import com.miso202402.front_miso_pf2_g8_sportapp.activities.MainActivity
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentChatBinding
import com.miso202402.front_miso_pf2_g8_sportapp.src.services.ApiService
import com.miso202402.front_miso_pf2_g8_sportapp.src.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ChatFragment : Fragment(), ClicListener_DoctorsTrainers {

    private var _binding: FragmentChatBinding? = null

    private lateinit var preferences: SharedPreferences
    private var domain: String = "https://g7o4mxf762.execute-api.us-east-1.amazonaws.com/prod/"
    private lateinit var doctorList : List<Doctors>
    private lateinit var entrenadorList : List<Trainers>
    lateinit var listener: ClicListener_DoctorsTrainers
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
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        doctorList = emptyList()
        entrenadorList = emptyList()
        listener = this
        typePlan = preferences.getData<String>("typePlan").toString()

        binding.imageButtonCalendarListProgramSessionsConsultationsFragment.setOnClickListener {
            val mainActivity = requireActivity() as? MainActivity
            mainActivity?.navigateToFragment(R.id.ListProgramSessionsConsultationsFragment, "Sesion y Chats")
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onResume() {
        super.onResume()
        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.showToolbarAndFab()
        fetchDoctorById()
        fetchEntrenadorById()
    }

    private fun fetchDoctorById() {
        progressBarVisible(true)
        CoroutineScope(Dispatchers.Main).launch {
            doctorList = getAllDocotros()
            Log.i("getAllDocotros.size: ", doctorList.size.toString())
            binding.recyclerviewDeportologoListProgramSessionsConsultationsFragment.setHasFixedSize(true)
            binding.recyclerviewDeportologoListProgramSessionsConsultationsFragment.layoutManager = LinearLayoutManager(context)
            binding.recyclerviewDeportologoListProgramSessionsConsultationsFragment.adapter =
                DoctorsTrainersAdapter (doctorList, emptyList(), listener)
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

    private fun fetchEntrenadorById() {
        progressBarVisible(true)
        CoroutineScope(Dispatchers.Main).launch {
            entrenadorList = getAllEntrenadores()
            Log.i("entrenadorList.size: ", entrenadorList.size.toString())
            binding.recyclerviewEntrenadorListProgramSessionsConsultationsFragment.setHasFixedSize(true)
            binding.recyclerviewEntrenadorListProgramSessionsConsultationsFragment.layoutManager = LinearLayoutManager(context)
            binding.recyclerviewEntrenadorListProgramSessionsConsultationsFragment.adapter =
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
            binding.progressBarDeportologo.visibility = View.VISIBLE
            binding.progressBarEntrenador.visibility = View.VISIBLE
        }else{
            binding.progressBarDeportologo.visibility = View.GONE
            binding.progressBarEntrenador.visibility = View.VISIBLE
        }
    }


    private fun openWhatsapp(number: String){
        val url = "https://api.whatsapp.com/send?phone=$number"
        val i = Intent(Intent.ACTION_VIEW)
        i.setData(Uri.parse(url))
        startActivity(i)
    }

    private fun showMessageDialog(activity: Context?, message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun onCListItemClick(view: View, doctor: Doctors?, trainer: Trainers?) {
        CoroutineScope(Dispatchers.Main).launch {
            Log.i("getDoctor yEntrneador", "Estoy Por aqui")
            if(doctor != null && doctor.id != ""){
                var numero = "+57 " + doctor.phone.toString()
                openWhatsapp(numero)
            }
            else{
                if(trainer != null && trainer.id != ""){
                    var numero = "+57 " + trainer.phone.toString()
                    openWhatsapp(numero)
                }
                else{
                    lifecycleScope.launch {
                        val message: String = "Error al traer el deportologo o el entrenador"
                        showMessageDialog(context, message)
                    }
                }
            }
        }
    }


}
package com.miso202402.SportApp.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.miso202402.SportApp.src.models.models.ConsultationsSessions
import com.miso202402.SportApp.src.models.models.Doctors
import com.miso202402.SportApp.src.models.models.Events
import com.miso202402.SportApp.src.models.models.Trainers
import com.miso202402.SportApp.src.models.response.GetConsultationByIdResponse
import com.miso202402.SportApp.src.models.response.GetDoctor
import com.miso202402.SportApp.src.models.response.GetTrainersByIdResponse
import com.miso202402.SportApp.src.utils.SharedPreferences
import com.miso202402.front_miso_pf2_g8_sportapp.R
import com.miso202402.front_miso_pf2_g8_sportapp.activities.MainActivity
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentInfoConsultationsprogramSessionsBinding
import com.miso202402.front_miso_pf2_g8_sportapp.src.services.ApiService
import com.miso202402.front_miso_pf2_g8_sportapp.src.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class InfoConsultationsprogramSessionsFragment : Fragment() {

    private var _binding: FragmentInfoConsultationsprogramSessionsBinding? = null

    private lateinit var preferences: SharedPreferences
    private var domain: String = "https://g7o4mxf762.execute-api.us-east-1.amazonaws.com/prod/"
    private lateinit var consultation_id: String;
    private lateinit var consultation : ConsultationsSessions

    private var vectorTipoDeporte  =  arrayOf("Atletismo", "Ciclismo")
    private lateinit var event: Events
    private var tipoDeporte : String? = null
    private var event_date : String? = null

    private lateinit var doctor :Doctors
    private lateinit var entrenador :Trainers

    private val binding get() = _binding!!
    override fun onAttach(context: Context) {
        super.onAttach(context)
        preferences = SharedPreferences(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoConsultationsprogramSessionsBinding.inflate(inflater, container, false)
        consultation_id = arguments?.getString("id").toString()
        Log.i("consultation.id entre", consultation_id)
        consultation = ConsultationsSessions("", "", "", "", "", "")

        getConsultationById()

        binding.buttonLinkInfoConsultationsprogramSessionsFragment.setOnClickListener {

            if(consultation.link != "" && consultation.id != ""){
                openWebURL(consultation.link.toString())
            }
            else{
                val utils = Utils()
                utils.showMessageDialog(context, "No Hay link con el cual navegar")
            }
        }

        binding.buttonChatInfoConsultationsprogramSessionsFragment.setOnClickListener {
            fetchgetDoctorAndEntrenadoprById(consultation.id_service_worker.toString())
        }

        binding.buttonAtrasInfoConsultationsprogramSessionsFragment.setOnClickListener {
            val mainActivity = requireActivity() as? MainActivity
            mainActivity?.navigateToFragment(R.id.ListProgramSessionsConsultationsFragment)
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

    private fun getConsultationById(){
        val utils = Utils()
        CoroutineScope(Dispatchers.IO).launch {
           // try {
                Log.i("Entre por aqui", consultation_id)
                val getConsultationById = utils.getRetrofit(domain)
                    .create(ApiService::class.java)
                    .getConsultationBYId(consultation_id)
                    .execute()
                val getConsultationByIdResponse = getConsultationById.body() as GetConsultationByIdResponse?

                Log.i("getConsultationById code: ", getConsultationByIdResponse?.code.toString())
                Log.i("getConsultationByIdResponse: ", getConsultationByIdResponse?.content?.id.toString())
                if (getConsultationByIdResponse?.code == 200){
                    consultation = getConsultationByIdResponse.content!!
                    Log.i("consultation", getConsultationByIdResponse.content!!.consultation_type.toString() )
                    val title: String = if(getConsultationByIdResponse.content?.consultation_type == "Presencial")
                        "Detalle de La Sesión Presencial:" else "Detalle de La Sesión Virtual:"
                   // binding.titleFragmentInfoConsultationsprogramSessionsFragment.setText(title)
                    binding.editTexLocationInfoConsultationsprogramSessionsFragment
                        .setText(consultation.link.toString())
                    binding.editTexDateInfoConsultationsprogramSessionsFragment
                        .setText(consultation.consultation_date.toString())
                    binding.editTexDeportologoInfoConsultationsprogramSessionsFragment
                        .setText(consultation.id_service_worker.toString())
                } else {
                    lifecycleScope.launch {
                        showMessageDialog(context, "Fallo la consulta de La session Programada")
                        val mainActivity = requireActivity() as? MainActivity
                        mainActivity?.navigateToFragment(R.id.ListProgramSessionsConsultationsFragment)
                    }


                }
          /*  } catch (e: Exception) {
                Log.e("error", e.message.toString())
                showMessageDialog(context, "Fallo la consulta de La session Programada")
                        val mainActivity = requireActivity() as? MainActivity
                        mainActivity?.navigateToFragment(R.id.ListProgramSessionsConsultationsFragment)

            }*/
        }
    }
    private fun openWebURL(inURL: String?) {
        val browse = Intent(Intent.ACTION_VIEW, Uri.parse(inURL))
        startActivity(browse)
    }

    private fun openWhatsapp(number: String){
        val url = "https://api.whatsapp.com/send?phone=$number"
        val i = Intent(Intent.ACTION_VIEW)
        i.setData(Uri.parse(url))
        startActivity(i)
    }

    private fun fetchgetDoctorAndEntrenadoprById(id: String?){
        CoroutineScope(Dispatchers.Main).launch {
            doctor = getDoctorById(id)
            if(doctor != null){
                var numero = "+57 " + doctor.phone.toString()
                openWhatsapp(numero)
            }
            else{
                entrenador = getEntreneadorById(id!!)
                var numero = "+57 " + entrenador.phone.toString()
                openWhatsapp(numero)
            }
        }
    }

    private suspend fun getDoctorById(id: String?): Doctors {
        val utils = Utils()
        return withContext(Dispatchers.IO) {
            try {
                val callGetDoctorsById = utils.getRetrofit(domain)
                    .create(ApiService::class.java)
                    .getDoctorsByID(id!!)
                    .execute()
                val getDoctor = callGetDoctorsById.body() as GetDoctor?
                Log.i("GetDoctor: ", getDoctor?.content?.id.toString())
                if (getDoctor?.code == 200) {
                    return@withContext (getDoctor?.content ?: null)!!
                } else {
                    return@withContext null!!
                }
            } catch (e: Exception) {
                Log.e("error", e.message.toString())
                return@withContext null!!
            }
        }
    }

    private suspend fun getEntreneadorById(id: String): Trainers {
        val utils = Utils()
        return withContext(Dispatchers.IO) {
            try {
                val callGetEntrenadorById = utils.getRetrofit(domain)
                    .create(ApiService::class.java)
                    .getTrainerssByID(id)
                    .execute()
                val getEntrenenador = callGetEntrenadorById.body() as GetTrainersByIdResponse?
                Log.i("getEntrenenador: ", getEntrenenador?.code.toString())
                if (getEntrenenador?.code == 200) {
                    return@withContext getEntrenenador?.content ?: null!!
                } else {
                    return@withContext null!!
                }
            } catch (e: Exception) {
                Log.e("error", e.message.toString())
                return@withContext null!!
            }
        }
    }

    private fun showMessageDialog(activity: Context?, message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }


}


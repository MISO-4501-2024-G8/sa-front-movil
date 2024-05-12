package com.miso202402.SportApp.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.miso202402.SportApp.src.models.models.ConsultationsSessions
import com.miso202402.SportApp.src.models.models.TrainingSession
import com.miso202402.SportApp.src.models.response.GetAllConsultationSessionsResponse
import com.miso202402.SportApp.src.models.response.GetAllUserTrainingSessionsResponse
import com.miso202402.SportApp.src.utils.ClicListener_ProgramConsultation
import com.miso202402.SportApp.src.utils.ClicTSListener
import com.miso202402.SportApp.src.utils.ProgramConsultationsAdapter
import com.miso202402.SportApp.src.utils.SharedPreferences
import com.miso202402.SportApp.src.utils.TrainingSessionAdapter
import com.miso202402.front_miso_pf2_g8_sportapp.R
import com.miso202402.front_miso_pf2_g8_sportapp.activities.MainActivity
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentCalendarBinding
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentListProgramSessionsConsultationsBinding
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentLoginBinding
import com.miso202402.front_miso_pf2_g8_sportapp.src.services.ApiService
import com.miso202402.front_miso_pf2_g8_sportapp.src.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ListProgramSessionsConsultationsFragment : Fragment(), ClicListener_ProgramConsultation {
    private var _binding: FragmentListProgramSessionsConsultationsBinding? = null
    private val binding get() = _binding!!

    private lateinit var preferences: SharedPreferences
    private var domain: String = "https://g7o4mxf762.execute-api.us-east-1.amazonaws.com/prod/"
    private lateinit var user_id: String;
    private lateinit var consultationsSessionsList : List<ConsultationsSessions>
    lateinit var listener: ClicListener_ProgramConsultation


    @RequiresApi(Build.VERSION_CODES.O)
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListProgramSessionsConsultationsBinding.inflate(inflater, container, false)
        user_id = preferences.getData<String>("id").toString()
        consultationsSessionsList = emptyList<ConsultationsSessions>()
        listener = this

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
        fetchConsultationByUserId()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        preferences = SharedPreferences(requireContext())
    }

    private suspend fun getAllConsultationByUserId(): List<ConsultationsSessions> {
        val utils = Utils()
        return withContext(Dispatchers.IO) {
            try {
                Log.i("user_id", user_id)
                val getAllConsultationByUserId = utils.getRetrofit(domain)
                    .create(ApiService::class.java)
                    .getConsultationBYuserId(user_id)
                    .execute()
                val getAllConsultationByUserIdResponse = getAllConsultationByUserId.body() as GetAllConsultationSessionsResponse?

                Log.i("getAllConsultationByUserIdResponse: ", getAllConsultationByUserIdResponse?.code.toString())
                Log.i("getAllConsultation Size: ", getAllConsultationByUserIdResponse?.content?.size.toString())
                if (getAllConsultationByUserIdResponse?.code == 200) {
                    return@withContext getAllConsultationByUserIdResponse?.content ?: emptyList()
                } else {
                    return@withContext emptyList()
                }
            } catch (e: Exception) {
                Log.e("error", e.message.toString())
                return@withContext emptyList()
            }
        }
    }

    private fun fetchConsultationByUserId() {
        progressBarVisible(true)
        CoroutineScope(Dispatchers.Main).launch {
            consultationsSessionsList = getAllConsultationByUserId()
            Log.i("consultationsSessionsList.size: ", consultationsSessionsList.size.toString())
            binding.recyclerviewListProgramSessionsConsultationsFragment.setHasFixedSize(true)
            binding.recyclerviewListProgramSessionsConsultationsFragment.layoutManager = LinearLayoutManager(context)
            binding.recyclerviewListProgramSessionsConsultationsFragment.adapter =
                ProgramConsultationsAdapter (consultationsSessionsList, listener)
            progressBarVisible(false)
        }
    }

    fun progressBarVisible(valueV:Boolean){
        if(valueV){
            binding.progressBar.visibility = View.VISIBLE

        }else{
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onCListItemClick(view: View, consultation: ConsultationsSessions) {

    }


}
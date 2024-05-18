package com.miso202402.SportApp.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.miso202402.SportApp.src.models.models.SportProfile
import com.miso202402.SportApp.src.models.models.TrainingPlan
import com.miso202402.SportApp.src.models.request.SportProfileRequest
import com.miso202402.SportApp.src.models.response.GetInfoUserResponse
import com.miso202402.SportApp.src.models.response.SportProfileResponse
import com.miso202402.SportApp.src.models.response.TrainingListPlansResponse
import com.miso202402.SportApp.src.utils.SharedPreferences
import com.miso202402.SportApp.src.utils.TrainingPlanAdapter
import com.miso202402.front_miso_pf2_g8_sportapp.R
import com.miso202402.front_miso_pf2_g8_sportapp.activities.MainActivity
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentChatBinding
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentGoalBinding
import com.miso202402.front_miso_pf2_g8_sportapp.src.services.ApiService
import com.miso202402.front_miso_pf2_g8_sportapp.src.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class GoalFragment : Fragment() {

    private var _binding: FragmentGoalBinding? = null
    private lateinit var preferences: SharedPreferences

    private lateinit var trainingPlanList : List<TrainingPlan>
    private var domainP: String = "http://lb-ms-py-workout-mngr-1790601522.us-east-1.elb.amazonaws.com/"
    private var domain: String = "http://lb-ms-py-training-mngr-157212315.us-east-1.elb.amazonaws.com/"
    private lateinit var sportProfile: SportProfile

    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        preferences = SharedPreferences(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoalBinding.inflate(inflater, container, false)

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

    }
    private fun showMessageDialog(activity: Context?, message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
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

                    if(_binding != null) {
                        withContext(Dispatchers.Main) {
                           // binding.recyclerviewTrainingSessionFragment.setHasFixedSize(true)
                           //binding.recyclerviewTrainingSessionFragment.layoutManager =
                                //LinearLayoutManager(context)
                           // binding.recyclerviewTrainingSessionFragment.adapter =
                               // TrainingPlanAdapter(trainingPlanListFilter, listener)
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

    private fun getOneSportProfileById(user_id: String){
        val utils = Utils()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val callGetOneSportProfileById = utils.getRetrofit(domainP)
                    .create(ApiService::class.java)
                    .getOneSportProfileById(user_id)
                    .execute()
                val getOneSportProfileByIdResponse = callGetOneSportProfileById.body() as SportProfileResponse?
                if (getOneSportProfileByIdResponse?.code == 200){
                    Log.i("callGetTrainingPlans","Antes de refrescar la lista")
                    sportProfile = getOneSportProfileByIdResponse?.user_profile!!
                    if(_binding != null) {
                        withContext(Dispatchers.Main) {
                            // binding.recyclerviewTrainingSessionFragment.setHasFixedSize(true)
                            //binding.recyclerviewTrainingSessionFragment.layoutManager =
                            //LinearLayoutManager(context)
                            // binding.recyclerviewTrainingSessionFragment.adapter =
                            // TrainingPlanAdapter(trainingPlanListFilter, listener)
                        }
                    }
                }else{
                    val message = getOneSportProfileByIdResponse?.message.toString()
                    Log.e("getAllTrainingPlans error: ", message)
                    utils.showMessageDialog(context, message)
                }
            } catch (e: Exception) {
                Log.e("error",e.message.toString())
            }
        }
    }

    private fun updateSportProfile(user_id: String, sportProfileRequest : SportProfileRequest){
        val utils = Utils()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val callupdateSportProfile = utils.getRetrofit(domainP)
                    .create(ApiService::class.java)
                    .updateSportProfile(user_id, sportProfileRequest)
                    .execute()
                val sportProfileResponseResponse = callupdateSportProfile.body() as SportProfileResponse?
                if (sportProfileResponseResponse?.code == 200){
                    Log.i("callGetTrainingPlans","Antes de refrescar la lista")
                    sportProfile = sportProfileResponseResponse?.user_profile!!
                    if(_binding != null) {
                        withContext(Dispatchers.Main) {
                            // binding.recyclerviewTrainingSessionFragment.setHasFixedSize(true)
                            //binding.recyclerviewTrainingSessionFragment.layoutManager =
                            //LinearLayoutManager(context)
                            // binding.recyclerviewTrainingSessionFragment.adapter =
                            // TrainingPlanAdapter(trainingPlanListFilter, listener)
                        }
                    }
                }else{
                    val message = sportProfileResponseResponse?.message.toString()
                    Log.e("getAllTrainingPlans error: ", message)
                    utils.showMessageDialog(context, message)
                }
            } catch (e: Exception) {
                Log.e("error",e.message.toString())
            }
        }
    }


    private fun getInfoUser(token: String, user_id: String){
        val utils = Utils()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val callgetInfoUser = utils.getRetrofit(domainP)
                    .create(ApiService::class.java)
                    .getInfoUser(token, user_id)
                    .execute()
                val callgetInfoUserResponse = callgetInfoUser.body() as GetInfoUserResponse?
                if (callgetInfoUserResponse?.code == 200){
                    Log.i("callGetTrainingPlans","Antes de refrescar la lista")
                   // sportProfile = callgetInfoUserResponse?.!!
                    if(_binding != null) {
                        withContext(Dispatchers.Main) {
                            // binding.recyclerviewTrainingSessionFragment.setHasFixedSize(true)
                            //binding.recyclerviewTrainingSessionFragment.layoutManager =
                            //LinearLayoutManager(context)
                            // binding.recyclerviewTrainingSessionFragment.adapter =
                            // TrainingPlanAdapter(trainingPlanListFilter, listener)
                        }
                    }
                }else{
                    val message = callgetInfoUserResponse?.message.toString()
                    Log.e("getAllTrainingPlans error: ", message)
                    utils.showMessageDialog(context, message)
                }
            } catch (e: Exception) {
                Log.e("error",e.message.toString())
            }
        }
    }





}
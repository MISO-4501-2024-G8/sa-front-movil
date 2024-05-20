package com.miso202402.SportApp.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.miso202402.SportApp.src.models.models.SportProfile
import com.miso202402.SportApp.src.models.models.SportProfileModel
import com.miso202402.SportApp.src.models.models.TrainingPlan
import com.miso202402.SportApp.src.models.request.SportProfileRequest
import com.miso202402.SportApp.src.models.response.SportProfileResponse
import com.miso202402.SportApp.src.utils.AddSportAdapterAdapter
import com.miso202402.SportApp.src.utils.ClicSportProfile
import com.miso202402.SportApp.src.utils.SharedPreferences
import com.miso202402.front_miso_pf2_g8_sportapp.R
import com.miso202402.front_miso_pf2_g8_sportapp.activities.MainActivity
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentAddSportProfileBinding
import com.miso202402.front_miso_pf2_g8_sportapp.src.services.ApiService
import com.miso202402.front_miso_pf2_g8_sportapp.src.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AddSportProfileFragment : Fragment(), ClicSportProfile {
    private var _binding: FragmentAddSportProfileBinding? = null
    private lateinit var preferences: SharedPreferences

    private lateinit var trainingPlanList : List<TrainingPlan>
    private var domainP: String = "http://lb-ms-py-workout-mngr-1790601522.us-east-1.elb.amazonaws.com/"
    private var domain: String = "http://lb-ms-py-training-mngr-157212315.us-east-1.elb.amazonaws.com/"
    private lateinit var sportProfile: SportProfile
    private lateinit var listener: ClicSportProfile
    private lateinit var user_id: String

    private lateinit var listMakeProfile : ArrayList<SportProfileModel>
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        preferences = SharedPreferences(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddSportProfileBinding.inflate(inflater, container, false)
        listener = this
        user_id = preferences.getData<String>("id").toString()
        fetchRecicly()

        binding.buttonAgregarGoalFragment.setOnClickListener {
            createSportProfile(user_id, listMakeProfile)
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

    }
    private fun preparevectopr(){
        listMakeProfile = ArrayList()
        listMakeProfile.add(SportProfileModel("Caminar", false,"Habitos Deportivos"))
        listMakeProfile.add(SportProfileModel("Trotar", false,"Habitos Deportivos"))
        listMakeProfile.add(SportProfileModel("Correr", false,"Habitos Deportivos"))
        listMakeProfile.add(SportProfileModel("Nadar", false,"Habitos Deportivos"))
        listMakeProfile.add(SportProfileModel("Bicicleta", false,"Habitos Deportivos"))
        listMakeProfile.add(SportProfileModel("Fractura", false, "Problemas Fisicos"))
        listMakeProfile.add(SportProfileModel("Esguinse", false, "Problemas Fisicos"))
        listMakeProfile.add(SportProfileModel("Lumbalgia", false, "Problemas Fisicos"))
        listMakeProfile.add(SportProfileModel("Articulación", false, "Problemas Fisicos"))
        listMakeProfile.add(SportProfileModel("Migrañas", false, "Problemas Fisicos"))
    }

    private fun fetchRecicly(){
        preparevectopr()
        binding.recyclerviewAddSportProfileFragment.setHasFixedSize(true)
        binding.recyclerviewAddSportProfileFragment.layoutManager =
        LinearLayoutManager(context)
        binding.recyclerviewAddSportProfileFragment.adapter =
            AddSportAdapterAdapter(listMakeProfile, listener)
    }
    private fun createSportProfile(user_id: String, list: ArrayList<SportProfileModel> ){
        val utils = Utils()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val callCreateSportProfile = utils.getRetrofit(domainP)
                    .create(ApiService::class.java)
                    .createSportProfile(SportProfileRequest(
                        user_id,
                        if (list[0].state == true) 1 else 0,
                        if (list[1].state == true) 1 else 0,
                        if (list[2].state == true) 1 else 0,
                        if (list[3].state == true) 1 else 0,
                        if (list[4].state == true) 1 else 0,
                        if (list[5].state == true) 1 else 0,
                        if (list[6].state == true) 1 else 0,
                        if (list[7].state == true) 1 else 0,
                        if (list[8].state == true) 1 else 0,
                        if (list[9].state == true) 1 else 0,
                        0.0.toFloat(),
                        0.0.toFloat(),
                        0,
                        0,
                        0,
                        ""
                    ))
                    .execute()
                val createSportProfileResponse = callCreateSportProfile.body() as SportProfileResponse?
                Log.i("Rest de Create profile", createSportProfileResponse?.user_profile?.user_id.toString())
                if (createSportProfileResponse?.code == 200){
                    Log.i("se creo con exito el profile",createSportProfileResponse.code.toString())
                    if(_binding != null) {
                        withContext(Dispatchers.Main) {
                            utils.showMessageDialog(context, "Se pudo crear el Perfil deportivo exitosamante")
                            val mainActivity = requireActivity() as? MainActivity
                            mainActivity?.navigateToFragment(R.id.GoalFragment, "Perfil Deportivo e indicadores")
                        }
                    }
                }else{
                    withContext(Dispatchers.Main) {
                        utils.showMessageDialog(context, "No se pudo crear el Perfil deportivo intente mas tarde")
                    }
                }
            } catch (e: Exception) {
                Log.e("error",e.message.toString())
            }
        }
    }

    override fun onCListSPClic(view: View, list: List<SportProfileModel>) {
        listMakeProfile = list as ArrayList<SportProfileModel>
    }

}
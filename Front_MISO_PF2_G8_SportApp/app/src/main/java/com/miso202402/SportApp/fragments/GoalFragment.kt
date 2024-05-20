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
import com.miso202402.SportApp.src.models.models.SportProfileModel
import com.miso202402.SportApp.src.models.models.TrainingPlan
import com.miso202402.SportApp.src.models.request.SportProfileRequest
import com.miso202402.SportApp.src.models.response.SportProfileResponse
import com.miso202402.SportApp.src.models.response.TrainingListPlansResponse
import com.miso202402.SportApp.src.utils.AddSportAdapterAdapter
import com.miso202402.SportApp.src.utils.ClicSportProfile
import com.miso202402.SportApp.src.utils.SharedPreferences
import com.miso202402.front_miso_pf2_g8_sportapp.R
import com.miso202402.front_miso_pf2_g8_sportapp.activities.MainActivity
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentGoalBinding
import com.miso202402.front_miso_pf2_g8_sportapp.src.services.ApiService
import com.miso202402.front_miso_pf2_g8_sportapp.src.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class GoalFragment : Fragment(), ClicSportProfile {

    private var _binding: FragmentGoalBinding? = null
    private lateinit var preferences: SharedPreferences

    private lateinit var trainingPlanList: List<TrainingPlan>
    private var domainP: String =
        "http://lb-ms-py-workout-mngr-1790601522.us-east-1.elb.amazonaws.com/"
    private var domain: String =
        "http://lb-ms-py-training-mngr-157212315.us-east-1.elb.amazonaws.com/"
    private lateinit var sportProfile: SportProfile
    private lateinit var user_id: String
    private lateinit var listMakeProfile: ArrayList<SportProfileModel>
    private lateinit var listener: ClicSportProfile

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoalBinding.inflate(inflater, container, false)

        listener = this
        user_id = preferences.getData<String>("id").toString()
        Log.i("user_id HH", user_id)
        getOneSportProfileById(user_id)

        binding.buttonAgregarGoalFragment.setOnClickListener {
            val mainActivity = requireActivity() as? MainActivity
            mainActivity?.navigateToFragment(
                R.id.AddSportProfileFragment,
                "Preferncias para el perfil  Deportivo"
            )
        }
        binding.buttonVisualizarGoalFragment.setOnClickListener {
            val mainActivity = requireActivity() as? MainActivity
            mainActivity?.navigateToFragment(
                R.id.HealthIndicators,
                "Indicadores para el perfil  Deportivo",
            )
        }

        binding.buttonActualizarGoalFragment.setOnClickListener {
            val sportProfileUpdate = SportProfileRequest(
                user_id,
                if (listMakeProfile[0].state == true) 1 else 0,
                if (listMakeProfile[1].state == true) 1 else 0,
                if (listMakeProfile[2].state == true) 1 else 0,
                if (listMakeProfile[3].state == true) 1 else 0,
                if (listMakeProfile[4].state == true) 1 else 0,
                if (listMakeProfile[5].state == true) 1 else 0,
                if (listMakeProfile[6].state == true) 1 else 0,
                if (listMakeProfile[7].state == true) 1 else 0,
                if (listMakeProfile[8].state == true) 1 else 0,
                if (listMakeProfile[9].state == true) 1 else 0,
                sportProfile.i_vo2max!!.toFloat(),
                sportProfile.i_ftp!!.toFloat(),
                sportProfile.i_total_practice_time,
                sportProfile.i_total_objective_achived,
                sportProfile.h_total_calories!!.toInt(),
                sportProfile.h_avg_bpm
            )
            updateSportProfile(user_id, sportProfileUpdate)
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        preferences = SharedPreferences(requireContext())
    }

    override fun onResume() {
        super.onResume()
        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.showToolbarAndFab()

    }

    private fun showMessageDialog(activity: Context?, message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    private fun getAllTrainingPlans() {
        val utils = Utils()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val callGetTrainingPlans = utils.getRetrofit(domain)
                    .create(ApiService::class.java)
                    .getTrainingPlans()
                    .execute()
                val getAllTrainingListPlansResponse =
                    callGetTrainingPlans.body() as TrainingListPlansResponse?
                if (getAllTrainingListPlansResponse?.code == 200) {
                    Log.i("callGetTrainingPlans", "Antes de refrescar la lista")
                    trainingPlanList = getAllTrainingListPlansResponse?.training_plans!!

                    if (_binding != null) {
                        withContext(Dispatchers.Main) {
                            // binding.recyclerviewTrainingSessionFragment.setHasFixedSize(true)
                            //binding.recyclerviewTrainingSessionFragment.layoutManager =
                            //LinearLayoutManager(context)
                            // binding.recyclerviewTrainingSessionFragment.adapter =
                            // TrainingPlanAdapter(trainingPlanListFilter, listener)
                        }
                    }
                } else {
                    Log.e(
                        "getAllTrainingPlans error: ",
                        getAllTrainingListPlansResponse?.message.toString()
                    )
                }
            } catch (e: Exception) {
                Log.e("error", e.message.toString())
            }
        }
    }

    private fun getOneSportProfileById(user_id: String) {
        val utils = Utils()
        Log.i("user_id2", user_id)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val callGetOneSportProfileById = utils.getRetrofit(domainP)
                    .create(ApiService::class.java)
                    .getOneSportProfileById(user_id)
                    .execute()
                val getOneSportProfileByIdResponse =
                    callGetOneSportProfileById.body() as SportProfileResponse?
                Log.i(
                    "Rest de Goal",
                    getOneSportProfileByIdResponse?.user_profile?.i_total_practice_time.toString()
                )
                if (getOneSportProfileByIdResponse?.code == 200) {
                    Log.i("getOneSportProfileByIdResponse", "Antes de refrescar la lista")
                    sportProfile = getOneSportProfileByIdResponse?.user_profile!!
                    if (_binding != null) {
                        binding.buttonVisualizarGoalFragment.isEnabled = true
                        withContext(Dispatchers.Main) {
                            progressbarVisible(true)
                            buttonAgregarVisible(false)
                            buttonActualizarVisible(true)
                            preparevector(sportProfile)
                            binding.recyclerviewGoalFragment.setHasFixedSize(true)
                            binding.recyclerviewGoalFragment.layoutManager =
                                LinearLayoutManager(context)
                            binding.recyclerviewGoalFragment.adapter =
                                AddSportAdapterAdapter(listMakeProfile, listener)
                            progressbarVisible(false)
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        utils.showMessageDialog(context, "No existe un Perfil deportivo Agreguelo")
                        buttonAgregarVisible(true)
                        binding.buttonVisualizarGoalFragment.isEnabled = false
                        buttonActualizarVisible(false)
                    }
                }
            } catch (e: Exception) {
                Log.e("error", e.message.toString())
            }
        }
    }

    private fun preparevector(sportProfile: SportProfile) {
        listMakeProfile = ArrayList()
        listMakeProfile.add(
            SportProfileModel(
                "Caminar",
                if (sportProfile.sh_caminar == 1) true else false,
                "Habitos Deportivos"
            )
        )
        listMakeProfile.add(
            SportProfileModel(
                "Trotar",
                if (sportProfile.sh_trotar == 1) true else false,
                "Habitos Deportivos"
            )
        )
        listMakeProfile.add(
            SportProfileModel(
                "Correr",
                if (sportProfile.sh_correr == 1) true else false,
                "Habitos Deportivos"
            )
        )
        listMakeProfile.add(
            SportProfileModel(
                "Nadar",
                if (sportProfile.sh_nadar == 1) true else false,
                "Habitos Deportivos"

            )
        )
        listMakeProfile.add(
            SportProfileModel(
                "Bicicleta",
                if (sportProfile.sh_bicicleta == 1) true else false,
                "Habitos Deportivos"
            )
        )
        listMakeProfile.add(
            SportProfileModel(
                "Fractura",
                if (sportProfile.pp_fractura == 1) true else false,
                "Problemas Fisicos"
            )
        )
        listMakeProfile.add(
            SportProfileModel(
                "Esguinse",
                if (sportProfile.pp_esguinse == 1) true else false,
                "Problemas Fisicos"
            )
        )
        listMakeProfile.add(
            SportProfileModel(
                "Lumbalgia",
                if (sportProfile.pp_lumbalgia == 1) true else false,
                "Problemas Fisicos"
            )
        )
        listMakeProfile.add(
            SportProfileModel(
                "Articulación",
                if (sportProfile.pp_articulacion == 1) true else false,
                "Problemas Fisicos"
            )
        )
        listMakeProfile.add(
            SportProfileModel(
                "Migrañas",
                if (sportProfile.pp_migranias == 1) true else false,
                "Problemas Fisicos"
            )
        )
    }

    private fun buttonAgregarVisible(valueV: Boolean) {
        if (valueV) {
            binding.buttonAgregarGoalFragment.visibility = View.VISIBLE
        } else {
            binding.buttonAgregarGoalFragment.visibility = View.GONE
        }
    }

    private fun buttonActualizarVisible(valueV: Boolean) {
        if (valueV) {
            binding.buttonActualizarGoalFragment.visibility = View.VISIBLE
        } else {
            binding.buttonActualizarGoalFragment.visibility = View.GONE
        }
    }

    private fun progressbarVisible(valueV: Boolean) {
        if (valueV) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun updateSportProfile(user_id: String, sportProfileRequest: SportProfileRequest) {
        val utils = Utils()
        CoroutineScope(Dispatchers.IO).launch {
            try {

                val callupdateSportProfile = utils.getRetrofit(domainP)
                    .create(ApiService::class.java)
                    .updateSportProfile(user_id, sportProfileRequest)
                    .execute()
                val sportProfileResponseResponse =
                    callupdateSportProfile.body() as SportProfileResponse?
                if (sportProfileResponseResponse?.code == 200) {
                    Log.i("callGetTrainingPlans", "Antes de refrescar la lista")
                    sportProfile = sportProfileResponseResponse?.user_profile!!
                    if (_binding != null) {
                        withContext(Dispatchers.Main) {
                            val message = "Se Actualizo el prefil deportivo"
                            Log.e("mensaje: ", message)
                            utils.showMessageDialog(context, message)
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        val message = sportProfileResponseResponse?.message.toString()
                        Log.e("getAllTrainingPlans error: ", message)
                        utils.showMessageDialog(context, message)
                    }
                }
            } catch (e: Exception) {
                Log.e("error", e.message.toString())
            }
        }
    }

    override fun onCListSPClic(view: View, list: List<SportProfileModel>) {
        listMakeProfile = list as ArrayList<SportProfileModel>
    }


}
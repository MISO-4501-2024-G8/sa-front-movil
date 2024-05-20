package com.miso202402.SportApp.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.miso202402.SportApp.src.models.models.SportProfile
import com.miso202402.SportApp.src.models.models.UserDetail
import com.miso202402.SportApp.src.models.request.SportProfileRequest
import com.miso202402.SportApp.src.models.response.GetInfoUserResponse
import com.miso202402.SportApp.src.models.response.SportProfileResponse
import com.miso202402.SportApp.src.utils.SharedPreferences
import com.miso202402.front_miso_pf2_g8_sportapp.R
import com.miso202402.front_miso_pf2_g8_sportapp.activities.MainActivity
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentHealthIndicatorsBinding
import com.miso202402.front_miso_pf2_g8_sportapp.src.services.ApiService
import com.miso202402.front_miso_pf2_g8_sportapp.src.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HealthIndicatorsFragment : Fragment() {
    private var _binding: FragmentHealthIndicatorsBinding? = null
    private lateinit var preferences: SharedPreferences

    private var domainP: String =
        "http://lb-ms-py-workout-mngr-1790601522.us-east-1.elb.amazonaws.com/"
    private var domain: String =
        "https://g7o4mxf762.execute-api.us-east-1.amazonaws.com/prod/"
    private lateinit var user_id: String
    private lateinit var token: String
    private lateinit var sportProfile: SportProfile
    private lateinit var userDetail: UserDetail

    private val binding get() = _binding!!
    override fun onAttach(context: Context) {
        super.onAttach(context)
        preferences = SharedPreferences(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHealthIndicatorsBinding.inflate(inflater, container, false)
        user_id = preferences.getData<String>("id").toString()
        token = preferences.getData<String>("token").toString()
        binding.buttonRecalcularHealthIndicatorsFragment.isEnabled = true
        userDetail = UserDetail(
          "",
          "",
          0, "0.0".toFloat(),
          0.0.toFloat(),
          "",
          "",
          "",
            "",
          0,
          "","",
          0,
          0,
          0 )
        getOneSportProfileById(user_id)

        binding.buttonRecalcularHealthIndicatorsFragment.setOnClickListener {
                getInfoUser(token, user_id)
        }

        binding.buttonAtrasHealthIndicatorsFragment.setOnClickListener {
            val mainActivity = requireActivity() as? MainActivity
            mainActivity?.navigateToFragment(R.id.GoalFragment, "Perfil Deportivo e indicadores")
        }

        return binding.root
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
                        withContext(Dispatchers.Main) {
                            val v2max = if(sportProfile.i_vo2max.toString() != "0.0") sportProfile.i_vo2max.toString() else ""
                            val ftp =  if(sportProfile.i_ftp.toString() != "0.0") sportProfile.i_ftp.toString() else ""
                            val total = if(sportProfile.i_total_practice_time.toString() != "0") sportProfile.i_total_practice_time.toString() else ""
                            val oc = if(sportProfile.i_total_objective_achived.toString() != "0") sportProfile.i_total_objective_achived.toString() else ""
                            val bmp = if(sportProfile.h_avg_bpm.toString() != "0.0") sportProfile.h_avg_bpm.toString() else ""
                            binding.editV02MaxHealthIndicatorsFragment.setText(v2max)
                            binding.editTexFTPHealthIndicatorsFragment.setText(ftp)
                            binding.editTexTPHealthIndicatorsFragment.setText(total)
                            binding.editTexOCHealthIndicatorsFragment.setText(oc)
                            binding.editTexbmpHealthIndicatorsFragment.setText(bmp.toString().split(".")[0])
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        utils.showMessageDialog(context, "fallo la busqueda del perfil deportivo")
                    }
                }
            } catch (e: Exception) {
                Log.e("error", e.message.toString())
            }
        }
    }
    private fun getInfoUser(token: String, user_id: String) {
        val utils = Utils()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.i("token", token)
                Log.i("user_id", user_id)
                val callgetInfoUser = utils.getRetrofit(domain)
                    .create(ApiService::class.java)
                    .getInfoUser("Bearer " + token, user_id)
                    .execute()
                val callgetInfoUserResponse = callgetInfoUser.body() as GetInfoUserResponse?
                Log.i("callgetInfoUserResponse", callgetInfoUserResponse?.code.toString())
                if (callgetInfoUserResponse?.code == 200) {
                    userDetail = callgetInfoUserResponse.detail!!
                    val TC: String  =  binding.editTexTPHealthIndicatorsFragment.text.toString()
                    val BMP: String  =  binding.editTexbmpHealthIndicatorsFragment.text.toString()
                    val TO: String = binding.editTexOCHealthIndicatorsFragment.text.toString()
                    val vatios =  binding.editTexWHealthIndicatorsFragment.text.toString()
                    val peso = userDetail.weight
                    val ftp = vatios.toInt() / (peso)
                    val tcm = TC.toInt() / 60
                    val bmp = BMP.toInt()

                    val V02Max: Double = (132.6
                            - (0.17 * peso)
                            - (0.39 * userDetail.age)
                            + (6.31 * if(userDetail.gender == "M" || userDetail.gender == "Mascu" ) 1 else 0 )
                            - (3.27 * tcm )
                            - (0.156 * bmp))
                    val calorias = 19 * 0.015 * peso
                    binding.editV02MaxHealthIndicatorsFragment.setText(V02Max.toString()).toString()
                    Log.i("bmp", bmp.toString())
                    Log.i("bmp split", bmp.toString().split(".")[0])
                    val bmpS: String = if (bmp.toString().contains(".")) bmp.toString().split(".")[0].toString() else
                        bmp.toString()
                    binding.editTexbmpHealthIndicatorsFragment.setText(bmpS).toString()
                    val ftpS: String = ftp.toString()
                    binding.editTexFTPHealthIndicatorsFragment.setText(ftpS).toString()
                    val sportProfileUpdate = SportProfileRequest(
                        sportProfile.user_id,
                        sportProfile.sh_caminar,
                        sportProfile.sh_trotar,
                        sportProfile.sh_correr,
                        sportProfile.sh_nadar,
                        sportProfile.sh_bicicleta,
                        sportProfile.pp_fractura,
                        sportProfile.pp_esguinse,
                        sportProfile.pp_lumbalgia,
                        sportProfile.pp_articulacion,
                        sportProfile.pp_articulacion,
                        V02Max.toFloat(),
                        ftp,
                        TC.toInt(),
                        TO.toInt(),
                        calorias.toInt(),
                        bmp.toString()
                    )
                    updateSportProfile(user_id, sportProfileUpdate)

                } else {
                    withContext(Dispatchers.Main) {
                        val message = callgetInfoUserResponse?.message.toString()
                        Log.e("callgetInfoUserResponse: ", message)
                        utils.showMessageDialog(context, message)
                    }
                }
            } catch (e: Exception) {
                Log.e("error", e.message.toString())
            }
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
                            val message = "Se realizo de nuevo el calculo con la ultima sesión"
                            Log.e("mensaje: ", message)
                            utils.showMessageDialog(context, message)
                            binding.buttonRecalcularHealthIndicatorsFragment.isEnabled = false
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


    private fun showMessageDialog(activity: Context?, message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

}
package com.miso202402.SportApp.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresExtension
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.miso202402.SportApp.src.models.models.Routs
import com.miso202402.SportApp.src.models.response.GetRoutsResponse
import com.miso202402.SportApp.src.utils.SharedPreferences
import com.miso202402.front_miso_pf2_g8_sportapp.R
import com.miso202402.front_miso_pf2_g8_sportapp.activities.MainActivity
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentInfoRoutBinding
import com.miso202402.front_miso_pf2_g8_sportapp.src.services.ApiService
import com.miso202402.front_miso_pf2_g8_sportapp.src.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InfoRoutFragment : Fragment() {
    private var _binding: FragmentInfoRoutBinding? = null
    private lateinit var preferences: SharedPreferences
    private val binding get() = _binding!!
    private var domain: String = "https://g7o4mxf762.execute-api.us-east-1.amazonaws.com/prod/"
    private var vectorTipoDeporte  =  arrayOf("Atletismo", "Ciclismo")
    private var tipoDeporte : String? = null
    private lateinit var rout: Routs
    private lateinit var route_id: String;
    private lateinit var user_id: String
    private var route_date : String? = null
    private var typePlan: String? = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        preferences = SharedPreferences(requireContext())
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoRoutBinding.inflate(inflater, container, false)
        route_id = arguments?.getString("rout_id").toString()
        Log.i("rout_id", route_id)
        user_id = preferences.getData<String>("id").toString()
        Log.i("user_id", user_id)
        typePlan = preferences.getData<String>("typePlan").toString()
        rout = Routs("", "","","","",
            "", "", "", "","","")
        binding.buttonAtrasRoutsFragment.setOnClickListener(){
            val mainActivity = requireActivity() as? MainActivity
            mainActivity?.navigateToFragment(R.id.CalendarFragment, "Detalle Ruta", null)
        }
        getRoutById(route_id)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                mostrarSnackbar("Utilizar los botones de la aplicacion para navegar.")
                return@setOnKeyListener true
            }
            false
        }
    }

    private fun mostrarSnackbar(mensaje: String) {
        view?.let {
            Snackbar.make(it, mensaje, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getRoutById(event_id: String){
        val utils = Utils()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val callGetRutaById = utils.getRetrofit(domain)
                    .create(ApiService::class.java)
                    .getRutaById(event_id)
                    .execute()
                val getRuta = callGetRutaById.body() as GetRoutsResponse?
                Log.i("Sali se la peticion getRuta", "Rest")
                Log.i("Sali a la peticion code ", getRuta?.code.toString())
                lifecycleScope.launch {
                    if (getRuta?.code == 200) {
                        rout = getRuta.content!!
                        binding.editTexNameRoutsFragment.setText(rout.route_name.toString())
                        binding.editTexDescriptionRoutsFragment.setText(rout.route_description.toString())
                        binding.editTexLocationRoutsFragment.setText(rout.route_location_A.toString() + "-" + rout.route_location_B.toString())
                        binding.sportRoutsFragment.setText(rout.sport.toString())
                        binding.dateRoutsFragment.setText(rout.route_date.toString())
                        binding.buttonLinkRoutsFragment.setOnClickListener{
                            val url = rout.link.toString()
                            Log.i("URL LINK ",url)
                            if(url == "") {
                                mostrarSnackbar("No hay un link asociado, intente mas tarde")
                            }else{
                                Log.i("URL TO OPEN ",url)
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                startActivity(intent)
                            }
                        }
                        binding.buttonMapaRoutsFragment.setOnClickListener {
                            val originLatLon = rout.route_latlon_A.toString().replace(" ","")
                            val destinationLatLon = rout.route_latlon_B.toString().replace(" ","")
                            var mode = "driving" // Puedes cambiar esto a "walking", "bicycling", o "transit" según el modo de transporte deseado
                            if(rout.sport.toString() == "Atletismo"){
                                mode = "walking"
                            }
                            val uri = "https://www.google.com/maps/dir/?api=1&origin=$originLatLon&destination=$destinationLatLon&travelmode=$mode"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                            startActivity(intent)
                        }
                    } else {
                        activity?.let {
                            utils.showMessageDialog(
                                it,
                                "Error Al traer la ruta, intente mas tarde"
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("error",e.message.toString())
            }
        }
    }

}
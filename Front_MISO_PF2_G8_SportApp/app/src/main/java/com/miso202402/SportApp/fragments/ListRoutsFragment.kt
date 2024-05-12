package com.miso202402.SportApp.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresExtension
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.miso202402.SportApp.src.models.models.Routs
import com.miso202402.SportApp.src.models.response.GetAllRutasResponse
import com.miso202402.SportApp.src.utils.ClicListener_routs
import com.miso202402.SportApp.src.utils.RoutsAdapter
import com.miso202402.front_miso_pf2_g8_sportapp.R
import com.miso202402.front_miso_pf2_g8_sportapp.activities.MainActivity
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentListRoutsBinding
import com.miso202402.front_miso_pf2_g8_sportapp.src.services.ApiService
import com.miso202402.front_miso_pf2_g8_sportapp.src.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ListRoutsFragment : Fragment(), ClicListener_routs {
    private var _binding: FragmentListRoutsBinding? = null
    private val binding get() = _binding!!
    private lateinit var routsList : List<Routs>
    private var domain: String = "https://g7o4mxf762.execute-api.us-east-1.amazonaws.com/prod/"
    lateinit var listener: ClicListener_routs
    private lateinit var token: String
    private lateinit var user_id : String

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListRoutsBinding.inflate(inflater, container, false)

        token = arguments?.getString("token").toString()
        user_id = arguments?.getString("user_id").toString()
        routsList = listOf()
        listener = this
        getAllRouts()
        binding.recyclerviewListRoutsFragment .setHasFixedSize(true)
        binding.recyclerviewListRoutsFragment.layoutManager = LinearLayoutManager(context)
        binding.recyclerviewListRoutsFragment.adapter = RoutsAdapter(routsList, listener)
        binding.imageButtonCalendarListRoutsFragment.setOnClickListener(){
            val mainActivity = requireActivity() as? MainActivity
            mainActivity?.navigateToFragment(R.id.ListEventsFragment)
        }

    return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonActualizarListRoutsFragment.setOnClickListener {
            getAllRouts()
            binding.recyclerviewListRoutsFragment .setHasFixedSize(true)
            binding.recyclerviewListRoutsFragment.layoutManager = LinearLayoutManager(context)
            binding.recyclerviewListRoutsFragment.adapter = RoutsAdapter(routsList, listener)
        }

        /*  binding.buttonAgregarListRoutsFragment.setOnClickListener {
            //val bundle = bundleOf("token" to  loginResponse?.token, "id" to loginResponse?.id)
            findNavController().navigate(R.id.action_ListRoutsFragment_to_AddRoutsFragment)
        }*/
        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                mostrarSnackbar("Utilizar los botones de la aplicacion para navegar.")
                return@setOnKeyListener true
            }
            false
        }
        binding.imageButtonRoutsListRoutsFragment.setOnClickListener{
            val bundle = bundleOf(
                "token" to token,
                "user_id" to user_id
            )
            findNavController().navigate(R.id.action_ListRoutsFragment_to_ListEventsFragment, bundle)
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

    private fun getAllRouts(){
        val utils = Utils()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val callGetAllRutas = utils.getRetrofit(domain)
                    .create(ApiService::class.java)
                    .getAllRutas()
                    .execute()
                val getAllRutasResponse = callGetAllRutas.body() as GetAllRutasResponse?
                if (getAllRutasResponse?.code == 200){
                    routsList = getAllRutasResponse?.content!!
                }


            } catch (e: Exception) {
                Log.e("error",e.message.toString())
            }
        }
    }
    override fun onCListItemClick(view: View, rout: Routs) {
        Log.i("Preuba", rout.route_name.toString())
        val bundle = bundleOf(
            "rout_id" to rout.id,
            "user_id" to user_id
        )
        findNavController().navigate(R.id.action_ListRoutsFragment_to_EditRoutsFragment, bundle)
    }



}

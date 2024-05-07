package com.miso202402.SportApp.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresExtension
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.miso202402.SportApp.src.models.models.Events
import com.miso202402.SportApp.src.models.models.Routs
import com.miso202402.SportApp.src.models.response.GetAllEventsResponse
import com.miso202402.SportApp.src.models.response.GetAllRutasResponse
import com.miso202402.SportApp.src.utils.ClickListener
import com.miso202402.SportApp.src.utils.ClickListener_routs
import com.miso202402.SportApp.src.utils.RoutsAdapter
import com.miso202402.SportApp.src.utils.WeeksAdapter
import com.miso202402.front_miso_pf2_g8_sportapp.R
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentListEventsBinding
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentListRoutsBinding
import com.miso202402.front_miso_pf2_g8_sportapp.src.services.ApiService
import com.miso202402.front_miso_pf2_g8_sportapp.src.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ListRoutsFragment : Fragment(), ClickListener_routs {
    private var _binding: FragmentListRoutsBinding? = null
    private val binding get() = _binding!!
    private lateinit var routsList : List<Routs>
    private var domain: String = "https://g7o4mxf762.execute-api.us-east-1.amazonaws.com/prod/"
    lateinit var listener: ClickListener_routs
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListRoutsBinding.inflate(inflater, container, false)

        routsList = listOf()
        listener = this
        getAllRouts()
        binding.recyclerviewListRoutsFragment .setHasFixedSize(true)
        binding.recyclerviewListRoutsFragment.layoutManager = LinearLayoutManager(context)
        binding.recyclerviewListRoutsFragment.adapter = RoutsAdapter(routsList, listener)

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

        binding.buttonAgregarListRoutsFragment.setOnClickListener {
            //val bundle = bundleOf("token" to  loginResponse?.token, "id" to loginResponse?.id)
            findNavController().navigate(R.id.action_ListRoutsFragment_to_AddRoutsFragment)
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
        val bundle = bundleOf("rout_id" to rout.id)
        findNavController().navigate(R.id.action_ListRoutsFragment_to_EditRoutsFragment, bundle)
    }



}

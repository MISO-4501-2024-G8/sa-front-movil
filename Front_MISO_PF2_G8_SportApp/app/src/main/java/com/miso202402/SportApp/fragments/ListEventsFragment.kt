package com.miso202402.SportApp.fragments

import android.content.Context
import android.net.http.HttpException
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresExtension
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.miso202402.SportApp.src.models.models.Events
import com.miso202402.SportApp.src.models.models.Routs
import com.miso202402.SportApp.src.models.response.GetAllEventsResponse
import com.miso202402.SportApp.src.utils.ClickListener
import com.miso202402.SportApp.src.utils.WeeksAdapter
import com.miso202402.front_miso_pf2_g8_sportapp.R
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentAddEventBinding
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentEditEventsBinding
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentListEventsBinding
import com.miso202402.front_miso_pf2_g8_sportapp.src.models.response.LoginResponse
import com.miso202402.front_miso_pf2_g8_sportapp.src.services.ApiService
import com.miso202402.front_miso_pf2_g8_sportapp.src.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException

class ListEventsFragment : Fragment(), ClickListener {
    private var _binding: FragmentListEventsBinding? = null
    private val binding get() = _binding!!

    private lateinit var eventList : List<Events>
    private var domain: String = "https://g7o4mxf762.execute-api.us-east-1.amazonaws.com/prod/"
    lateinit var listener: ClickListener


    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListEventsBinding.inflate(inflater, container, false)
        eventList = listOf()
        listener = this
        getAllEvents()
        binding.recyclerviewListEventsFragment.setHasFixedSize(true)
        binding.recyclerviewListEventsFragment.layoutManager = LinearLayoutManager(context)
        binding.recyclerviewListEventsFragment.adapter = WeeksAdapter(eventList, listener)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonActualizarListEventsFragment.setOnClickListener {
            getAllEvents()
            binding.recyclerviewListEventsFragment.setHasFixedSize(true)
            binding.recyclerviewListEventsFragment.layoutManager = LinearLayoutManager(context)
            binding.recyclerviewListEventsFragment.adapter = WeeksAdapter(eventList, listener)
        }

        binding.buttonAgregarListEventsFragment.setOnClickListener {
            //val bundle = bundleOf("token" to  loginResponse?.token, "id" to loginResponse?.id)
            findNavController().navigate(R.id.action_ListEventsFragment_to_AddEventFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getAllEvents(){
        val utils = Utils()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val callGetAllEventos = utils.getRetrofit(domain)
                    .create(ApiService::class.java)
                    .getAllEventos()
                    .execute()
                val getAllEventosResponse = callGetAllEventos.body() as GetAllEventsResponse?
                if (getAllEventosResponse?.code == 200){
                    eventList = getAllEventosResponse?.content!!
                }


            } catch (e: Exception) {
                Log.e("error",e.message.toString())
            }
        }
    }

    override fun onCListItemClick(view: View, event: Events) {
        Log.i("Preuba", event.event_name.toString())
        val bundle = bundleOf("event_id" to event.id )
        findNavController().navigate(R.id.action_ListEventsFragment_to_EditEventsFragment, bundle)
    }


}
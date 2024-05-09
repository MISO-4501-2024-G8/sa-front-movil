package com.miso202402.SportApp.src.utils

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.miso202402.SportApp.fragments.ListEventsFragment
import com.miso202402.SportApp.src.models.models.TrainingSession
import com.miso202402.front_miso_pf2_g8_sportapp.R

class TrainingSessionAdapter (private val listTrainingSessions:  List<TrainingSession>, val clickListener: ClicTSListener ) : RecyclerView.Adapter<TrainingSessionAdapter.ViewHolder>(){

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvName: TextView
        var tvLocation: TextView
        var tvDate: TextView
        var tvDescription: TextView
        var isEvento: TextView
        var isRuta: TextView
        var isAtletismo: TextView
        var isCiclismo: TextView
        var isVirtual: TextView
        var isPresencial: TextView
        init {
            tvName = view.findViewById(R.id.textViewName_cardView)
            tvLocation = view.findViewById(R.id.textViewLocation_cardView)
            tvDate = view.findViewById(R.id.textViewDate_cardView)
            tvDescription = view.findViewById(R.id.textViewdescription_cardView)
            isEvento = view.findViewById(R.id.textViewEvento_cardView)
            isRuta = view.findViewById(R.id.textViewRuta_cardView)
            isAtletismo = view.findViewById(R.id.textViewAtletismo_cardView)
            isCiclismo = view.findViewById(R.id.textViewCiclismo_cardView)
            isVirtual = view.findViewById(R.id.textViewVirtual_cardView)
            isPresencial = view.findViewById(R.id.textViewPresencial_cardView)

        }
    }

    @SuppressLint("ResourceType")
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): TrainingSessionAdapter.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.training_session_item, viewGroup, false)

        return TrainingSessionAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: TrainingSessionAdapter.ViewHolder, position: Int) {

        viewHolder.tvName.text = "ID Evento "+ position + 1 +": " + listTrainingSessions[position].id_event.toString()
        viewHolder.tvLocation.text = "Categoria: "+ listTrainingSessions[position].event_category.toString()
        viewHolder.tvDate.text = "Fecha: " + listTrainingSessions[position].session_date.toString()
        viewHolder.tvDescription.text = "Descripcion: desc test"
        val esEvento = true
        val esRuta = false
        val esAtletismo = false
        val esCiclismo = true
        val esVirtual = false
        val esPresencial = true
        if(esEvento){
            viewHolder.isEvento.visibility = View.VISIBLE
        }else{
            viewHolder.isEvento.visibility = View.GONE
        }
        if(esRuta){
            viewHolder.isRuta.visibility = View.VISIBLE
        }else{
            viewHolder.isRuta.visibility = View.GONE
        }
        if(esAtletismo){
            viewHolder.isAtletismo.visibility = View.VISIBLE
        }else{
            viewHolder.isAtletismo.visibility = View.GONE
        }
        if(esCiclismo){
            viewHolder.isCiclismo.visibility = View.VISIBLE
        }else{
            viewHolder.isCiclismo.visibility = View.GONE
        }
        if(esVirtual){
            viewHolder.isVirtual.visibility = View.VISIBLE
        }else{
            viewHolder.isVirtual.visibility = View.GONE
        }
        if(esPresencial){
            viewHolder.isPresencial.visibility = View.VISIBLE
        }else{
            viewHolder.isPresencial.visibility = View.GONE
        }
        viewHolder.itemView.setOnClickListener() {
            val trainingSession = listTrainingSessions.get(position)
            clickListener.onCListTSItemClic(it, trainingSession)
        }
    }

    override fun getItemCount(): Int {
        return listTrainingSessions.size
    }
}
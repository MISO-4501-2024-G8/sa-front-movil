package com.miso202402.SportApp.src.utils

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.miso202402.SportApp.src.models.models.ConsultationsSessions
import com.miso202402.front_miso_pf2_g8_sportapp.R

class ProgramConsultationsAdapter  (private val listEvents:  List<ConsultationsSessions>, val clicListener: ClicListener_ProgramConsultation ) : RecyclerView.Adapter<ProgramConsultationsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textViewName: TextView
        var textViewDescription: TextView
        init {
            textViewName = view.findViewById(R.id.textViewName_cardView)
            textViewDescription = view.findViewById(R.id.textViewdescription_cardView)
        }
    }

    @SuppressLint("ResourceType")
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.frame_textview, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        var realPosition: Int = position + 1

        viewHolder.textViewName.text = "Sesión "+ realPosition.toString() +": " + listEvents[position].consultation_type
        viewHolder.textViewDescription.text = "Fecha: "+ listEvents[position].consultation_date.toString()
        viewHolder.itemView.setOnClickListener() {
            val event = listEvents.get(position)
            clicListener.onCListItemClick(it, event)
        }
    }

    override fun getItemCount(): Int {
        return listEvents.size
    }
}
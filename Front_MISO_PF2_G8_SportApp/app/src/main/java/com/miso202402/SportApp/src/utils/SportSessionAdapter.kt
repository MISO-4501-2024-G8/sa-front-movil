package com.miso202402.SportApp.src.utils

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.miso202402.SportApp.src.models.models.SportSession
import com.miso202402.SportApp.src.models.models.TrainingPlan
import com.miso202402.front_miso_pf2_g8_sportapp.R

class SportSessionAdapter (
    private val listSportSession: List<SportSession>,
    val clickListener: ClickListener_SportSession) : RecyclerView.Adapter<SportSessionAdapter.ViewHolder>(){

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvName: TextView
        var tvDescription: TextView
        var tvDate:TextView
        init {
            tvName = view.findViewById(R.id.textViewName_cardView)
            tvDescription = view.findViewById(R.id.textViewdescription_cardView)
            tvDate = view.findViewById(R.id.textViewDate_cardView)
        }
    }

    @SuppressLint("ResourceType")
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SportSessionAdapter.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.sport_session_item, viewGroup, false)
        return SportSessionAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: SportSessionAdapter.ViewHolder, position: Int) {
        val id_sport_session = listSportSession[position].id
        val realPosition = position + 1;
        viewHolder.tvName.text = "Sesion Deportiva - $realPosition"
        viewHolder.tvDescription.text = "Semana: " + listSportSession[position].week.toString() + " Dia: " + listSportSession[position].day
        viewHolder.tvDate.text = "Fecha: " + listSportSession[position].session_event
        viewHolder.itemView.setOnClickListener() {
            val sportSession = listSportSession.get(position)
            clickListener.onCListItemClick(it, sportSession)
        }
    }

    override fun getItemCount(): Int {
        return listSportSession.size
    }
}
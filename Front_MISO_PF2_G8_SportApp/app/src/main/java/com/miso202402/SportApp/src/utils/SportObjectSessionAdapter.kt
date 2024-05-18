package com.miso202402.SportApp.src.utils

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.miso202402.SportApp.src.models.models.SportObjectiveSession
import com.miso202402.front_miso_pf2_g8_sportapp.R

class SportObjectSessionAdapter (
    private val listSportObjectiveSession: List<SportObjectiveSession>,
    val clickListener: ClickListener_SportObjectiveSession) : RecyclerView.Adapter<SportObjectSessionAdapter.ViewHolder>(){

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvName: TextView
        var tvDescription: TextView
        var cardViewObjective: androidx.cardview.widget.CardView
        init {
            tvName = view.findViewById(R.id.textViewName_cardView)
            tvDescription = view.findViewById(R.id.textViewdescription_cardView)
            cardViewObjective = view.findViewById(R.id.cardViewObjective)
        }
    }

    @SuppressLint("ResourceType")
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SportObjectSessionAdapter.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.sport_objective_session_item, viewGroup, false)
        return SportObjectSessionAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: SportObjectSessionAdapter.ViewHolder, position: Int) {
        val id_sport_objective_session = listSportObjectiveSession[position].id
        val objective_item = listSportObjectiveSession[position]
        val realPosition = position + 1
        val backgroundColorA = ContextCompat.getColor(viewHolder.itemView.context, R.color.objA)
        val backgroundColor = ContextCompat.getColor(viewHolder.itemView.context, R.color.white)

        viewHolder.tvName.text = "Objetivo $realPosition"
        viewHolder.tvDescription.text = "Realizar " + objective_item.instruction_time + "' " + objective_item.instruction_description
        Log.i("SportObjectSessionAdapter","Target achived: " + objective_item.target_achieved.toString())
        if(objective_item.target_achieved == 0){
            viewHolder.tvName.setTextColor(Color.BLACK)
            viewHolder.tvDescription.setTextColor(Color.BLACK)
            viewHolder.cardViewObjective.setCardBackgroundColor(backgroundColor)
        } else if(objective_item.target_achieved == 1){
            viewHolder.tvName.setTextColor(Color.WHITE)
            viewHolder.tvDescription.setTextColor(Color.WHITE)
            viewHolder.cardViewObjective.setCardBackgroundColor(backgroundColorA)
        }
        viewHolder.itemView.setOnClickListener() {
            val sportObjectiveSession = listSportObjectiveSession.get(position)
            clickListener.onCListItemClick(it, sportObjectiveSession)
        }
    }

    override fun getItemCount(): Int {
        return listSportObjectiveSession.size
    }
}
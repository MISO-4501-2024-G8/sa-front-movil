package com.miso202402.SportApp.src.utils

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.miso202402.SportApp.src.models.models.Objective
import com.miso202402.front_miso_pf2_g8_sportapp.R

class RestDetailAdapter (
    private val objectiveList:  List<Objective> ) : RecyclerView.Adapter<RestDetailAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var dayName: TextView
        var repeatsName: TextView
        var instrucciones: TextView
        init {
            dayName = view.findViewById(R.id.dayName)
            repeatsName = view.findViewById(R.id.repeatsName)
            instrucciones = view.findViewById(R.id.instrucciones)
        }
    }

    @SuppressLint("ResourceType")
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.rest_objective_week_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.dayName.text = objectiveList[position].day
        viewHolder.repeatsName.text = "Repeticiones: "+ objectiveList[position].repeats.toString()
        var instrucciones: String = ""
        for(instruccion in objectiveList[position].instructions!!){
            instrucciones += instruccion.instruction_time.toString() + "' " + instruccion.instruction_description + ", "
        }
        viewHolder.instrucciones.text = "Instrucciones: " + instrucciones
    }

    override fun getItemCount(): Int {
        return objectiveList.size
    }
}
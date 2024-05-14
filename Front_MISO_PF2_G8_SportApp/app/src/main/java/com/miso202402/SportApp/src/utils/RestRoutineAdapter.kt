package com.miso202402.SportApp.src.utils

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.miso202402.SportApp.src.models.models.FoodRoutine
import com.miso202402.SportApp.src.models.models.RestRoutine
import com.miso202402.front_miso_pf2_g8_sportapp.R

class RestRoutineAdapter (
    private val restRoutineList:  List<RestRoutine>,
    val clickListener: ClickListener_restroutines
) : RecyclerView.Adapter<RestRoutineAdapter.ViewHolder>() {
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
            .inflate(R.layout.rest_routine_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        var realPosition: Int = position + 1

        viewHolder.textViewName.text = "Rutina "+ realPosition.toString() +": " + restRoutineList[position].name
        viewHolder.textViewDescription.text = "Descripcion: "+ restRoutineList[position].description
        viewHolder.itemView.setOnClickListener() {
            val event = restRoutineList.get(position)
            clickListener.onListItemClick(it, event, position)
        }
        viewHolder.itemView.setOnLongClickListener(){
            val event = restRoutineList.get(position)
            clickListener.onListItemLongClick(it,event,position)
            true
        }
    }

    override fun getItemCount(): Int {
        return restRoutineList.size
    }
}
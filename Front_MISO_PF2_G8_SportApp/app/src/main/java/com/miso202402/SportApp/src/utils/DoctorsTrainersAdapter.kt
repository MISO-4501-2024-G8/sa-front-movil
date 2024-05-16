package com.miso202402.SportApp.src.utils

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.miso202402.SportApp.src.models.models.Doctors
import com.miso202402.SportApp.src.models.models.Trainers
import com.miso202402.front_miso_pf2_g8_sportapp.R

class DoctorsTrainersAdapter  (private val listDoctors:  List<Doctors>, private val listTrainers:  List<Trainers>, val clicListener: ClicListener_DoctorsTrainers ) : RecyclerView.Adapter<DoctorsTrainersAdapter.ViewHolder>() {

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
        if(listDoctors.size > 1)  {
            viewHolder.textViewName.text = "Doctor "+ realPosition.toString() +": " + listDoctors[position].id
            viewHolder.textViewDescription.text = "Telefono: "+ listDoctors[position].phone.toString()

            viewHolder.itemView.setOnClickListener() {
                val event = listDoctors.get(position)
                clicListener.onCListItemClick(it, event, null)
            }
            //se debe consultar tambien la hora de disponibilidad del doctor
        } else {
            viewHolder.textViewName.text = "Entrenador "+ realPosition.toString() +": " + listTrainers[position].id
            viewHolder.textViewDescription.text = "Telefono: "+ listTrainers[position].phone.toString()

            viewHolder.itemView.setOnClickListener() {
                val event = listTrainers.get(position)
                clicListener.onCListItemClick(it, null, event)
            }
            //se debe consultar tambien la hora de disponibilidad del entrenador
        }

    }

    override fun getItemCount(): Int {
        if(listDoctors.size > 1)  {
            return listDoctors.size
        }
        else{
            return listTrainers.size
        }
    }
}
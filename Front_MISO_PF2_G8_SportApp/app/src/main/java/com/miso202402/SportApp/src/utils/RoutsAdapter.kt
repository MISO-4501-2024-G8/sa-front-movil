package com.miso202402.SportApp.src.utils

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.miso202402.SportApp.src.models.models.Routs
import com.miso202402.front_miso_pf2_g8_sportapp.R

class RoutsAdapter (private val routsList:  List<Routs>, val clickListener: ClicListener_routs ) : RecyclerView.Adapter<RoutsAdapter.ViewHolder>() {
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

        viewHolder.textViewName.text = "Ruta "+ realPosition.toString() +": " + routsList[position].route_name.toString()
        viewHolder.textViewDescription.text = "Descripcion: "+ routsList[position].route_description.toString()
        viewHolder.itemView.setOnClickListener() {
            val event = routsList.get(position)
            clickListener.onCListItemClick(it, event)
        }
    }

    override fun getItemCount(): Int {
        return routsList.size
    }
}
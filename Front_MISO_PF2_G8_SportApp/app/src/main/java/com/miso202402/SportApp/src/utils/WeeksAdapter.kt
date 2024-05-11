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
import com.miso202402.SportApp.src.models.models.Events
import com.miso202402.front_miso_pf2_g8_sportapp.R

class WeeksAdapter (private val listEvents:  List<Events>, val clickListener: ClickListener ) : RecyclerView.Adapter<WeeksAdapter.ViewHolder>() {
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

        viewHolder.textViewName.text = "Evento "+ realPosition.toString() +": " + listEvents[position].event_name.toString()
        viewHolder.textViewDescription.text = "Descripcion: "+ listEvents[position].event_description.toString()
        viewHolder.itemView.setOnClickListener() {
            val event = listEvents.get(position)
            clickListener.onCListItemClick(it, event)
        }
    }

    override fun getItemCount(): Int {
        return listEvents.size
    }
}
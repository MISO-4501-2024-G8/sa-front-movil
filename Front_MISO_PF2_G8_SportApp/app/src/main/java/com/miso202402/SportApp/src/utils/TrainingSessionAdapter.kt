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
import com.miso202402.SportApp.src.models.models.Routs
import com.miso202402.SportApp.src.models.models.TrainingSession
import com.miso202402.front_miso_pf2_g8_sportapp.R

class
TrainingSessionAdapter (
    private val listTrainingSessions:  List<TrainingSession>,
    private val listEventos: List<Events>,
    private val listRutas: List<Routs>,
    val clickListener: ClicTSListener ) : RecyclerView.Adapter<TrainingSessionAdapter.ViewHolder>(){

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

    fun List<Events>.findFirstEventById(id: String): Events? {
        return this.firstOrNull { it.id == id }
    }
    fun List<Routs>.findFirstRouteById(id: String): Routs? {
        return this.firstOrNull { it.id == id }
    }
    override fun onBindViewHolder(viewHolder: TrainingSessionAdapter.ViewHolder, position: Int) {
        var esEvento = false
        var esRuta = false
        var esAtletismo = false
        var esCiclismo = false
        var esVirtual = false
        var esPresencial = false
        val id_event = listTrainingSessions[position].id_event.toString()
        val sport_type = listTrainingSessions[position].sport_type.toString()
        val categoria = listTrainingSessions[position].event_category.toString()
        viewHolder.tvDate.text = "Fecha: " + listTrainingSessions[position].session_date.toString()

        if(categoria == "evento"){
            esEvento = true
            val evento = listEventos.findFirstEventById(id_event)
            viewHolder.tvName.text = evento?.event_name
            viewHolder.tvLocation.text = "Lugar: " + evento?.event_location
            viewHolder.tvDescription.text = "Descripcion: " + evento?.event_description
            if(evento?.event_type == "virtual"){
                esVirtual = true
            }else if(evento?.event_type == "presencial"){
                esPresencial = true
            }
        } else if (categoria == "ruta"){
            esRuta = true
            val ruta = listRutas.findFirstRouteById(id_event)
            viewHolder.tvName.text = ruta?.route_name
            viewHolder.tvLocation.text = "Lugar: " + ruta?.route_location_A + " - " + ruta?.route_location_B
            viewHolder.tvDescription.text = "Descripcion: " + ruta?.route_description
            if(ruta?.route_type == "virtual"){
                esVirtual = true
            }else if(ruta?.route_type == "presencial"){
                esPresencial = true
            }
        }

        if(sport_type == "Atletismo"){
            esAtletismo = true
        } else if(sport_type == "Ciclismo"){
            esCiclismo = true
        }

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
package com.miso202402.SportApp.src.utils

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.miso202402.SportApp.src.models.models.Events
import com.miso202402.SportApp.src.models.models.Routs
import com.miso202402.SportApp.src.models.models.TrainingPlan
import com.miso202402.front_miso_pf2_g8_sportapp.R

class TrainingPlanAdapter (
    private val listTrainingPlans: List<TrainingPlan>,
    val clicTPListener: ClicTPListener) : RecyclerView.Adapter<TrainingPlanAdapter.ViewHolder>(){

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvName: TextView
        var tvDescription: TextView
        var isAtletismo: TextView
        var isCiclismo: TextView
        init {
            tvName = view.findViewById(R.id.textViewName_cardView)
            tvDescription = view.findViewById(R.id.textViewdescription_cardView)
            isAtletismo = view.findViewById(R.id.textViewAtletismo_cardView)
            isCiclismo = view.findViewById(R.id.textViewCiclismo_cardView)
        }
    }

    @SuppressLint("ResourceType")
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): TrainingPlanAdapter.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.training_plan_item, viewGroup, false)
        return TrainingPlanAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: TrainingPlanAdapter.ViewHolder, position: Int) {
        var esAtletismo = false
        var esCiclismo = false
        val id_training_plan = listTrainingPlans[position].id
        viewHolder.tvName.text = listTrainingPlans[position].name
        viewHolder.tvDescription.text = "Descripcion: " + listTrainingPlans[position].description
        val sport_type = listTrainingPlans[position].sport.toString()
        if(sport_type == "Atletismo"){
            esAtletismo = true
        } else if(sport_type == "Ciclismo"){
            esCiclismo = true
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

        viewHolder.itemView.setOnClickListener() {
            val trainingPlan = listTrainingPlans.get(position)
            clicTPListener.onCListItemClick(it, trainingPlan)
        }
    }

    override fun getItemCount(): Int {
        return listTrainingPlans.size
    }
}
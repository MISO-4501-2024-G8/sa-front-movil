package com.miso202402.SportApp.src.utils

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.miso202402.SportApp.src.models.models.FoodObjective
import com.miso202402.front_miso_pf2_g8_sportapp.R

class FoodRoutineDetailAdapter (
    private val foodObjectiveList:  List<FoodObjective> ) : RecyclerView.Adapter<FoodRoutineDetailAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var dayName: TextView
        var alimentos: TextView
        init {
            dayName = view.findViewById(R.id.dayName)
            alimentos = view.findViewById(R.id.alimentos)
        }
    }

    @SuppressLint("ResourceType")
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.food_objective_week_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.dayName.text = foodObjectiveList[position].day
        var alimentos: String = ""
        for(food in foodObjectiveList[position].foods!!){
            alimentos += food.food + ", "
        }
        viewHolder.alimentos.text = "Alimentos: " + alimentos
    }

    override fun getItemCount(): Int {
        return foodObjectiveList.size
    }
}
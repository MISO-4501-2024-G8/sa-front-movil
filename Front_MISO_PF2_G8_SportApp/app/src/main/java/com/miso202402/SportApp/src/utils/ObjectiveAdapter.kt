package com.miso202402.SportApp.src.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.miso202402.SportApp.src.models.models.Instruction
import com.miso202402.SportApp.src.models.models.Objective
import com.miso202402.front_miso_pf2_g8_sportapp.R

class ObjectiveAdapter (
    private val objectiveList: List<Objective>,
    private val context: Context,
    private val clickListener: ClickListener_Objective
) : RecyclerView.Adapter<ObjectiveAdapter.ViewHolder>(),ClicListener_Instruction
{
    lateinit var listener:ClicListener_Instruction
    private lateinit var instructionAdapter: InstructionAdapter
    lateinit var instructionList: MutableList<Instruction>
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkBoxDay: CheckBox = view.findViewById(R.id.checkBoxDay)
        val qtyText: TextView = view.findViewById(R.id.qtyText)
        val btnMinusRepeticiones: ImageButton = view.findViewById(R.id.btnMinusRepeticiones)
        val btnPlusRepeticiones: ImageButton = view.findViewById(R.id.btnPlusRepeticiones)
        val btnPlusInstruction: ImageButton = view.findViewById(R.id.btnPlusInstruction)
        val recyclerViewInstructions: RecyclerView = view.findViewById(R.id.recyclerview_ListInstructions)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.training_plan_week_item, parent, false)
        listener = this
        instructionList = mutableListOf()
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val objective = objectiveList[position]
        if(objective.repeats == null){
            objective.repeats = 0
        }
        instructionList = objective.instructions?.toMutableList() ?: mutableListOf()
        holder.checkBoxDay.text = objective.day
        holder.checkBoxDay.isChecked = false
        holder.qtyText.text = objective.repeats.toString()
        holder.btnMinusRepeticiones.isEnabled = objective.repeats!! > 0

        instructionAdapter = InstructionAdapter(objective.instructions ?: emptyList(), this)
        holder.recyclerViewInstructions.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = instructionAdapter
            setHasFixedSize(true)
        }

        with(holder) {
            btnPlusInstruction.setOnClickListener {
                clickListener.onAddItemClick(it, objective)
            }

            btnMinusRepeticiones.setOnClickListener {
                objective.repeats = objective.repeats!! - 1
                qtyText.text = objective.repeats.toString()
                btnMinusRepeticiones.isEnabled = objective.repeats!! > 0
            }

            btnPlusRepeticiones.setOnClickListener {
                objective.repeats = objective.repeats!! + 1
                qtyText.text = objective.repeats.toString()
                btnMinusRepeticiones.isEnabled = true
            }

            checkBoxDay.setOnClickListener {
                checkBoxDay.isChecked = !checkBoxDay.isChecked
            }
        }
    }

    override fun getItemCount(): Int {
        return objectiveList.size
    }

    override fun onRemoveItemClick(view: View, instruction: Instruction){
        Log.i("ObjectiveAdapter","onRemoveItemClick")
        val position = instructionList.indexOf(instruction)

        if (position != -1) {
            instructionList.removeAt(position)
            instructionAdapter.notifyDataSetChanged()
        } else {
            Log.e("InstructionAdapter", "Instrucción no encontrada en la lista")
        }
    }
    override fun onCListItemClick(view: View, instruction: Instruction){
        Log.i("ObjectiveAdapter","onCListItemClick")
    }
}
package com.miso202402.SportApp.src.utils

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.miso202402.SportApp.src.models.models.Instruction
import com.miso202402.SportApp.src.models.models.Routs
import com.miso202402.front_miso_pf2_g8_sportapp.R

class InstructionAdapter (
    private val instructionList: List<Instruction>,
    private val clickListener: ClicListener_Instruction
) : RecyclerView.Adapter<InstructionAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewAction: TextView = view.findViewById(R.id.actionText)
        val textViewTime: TextView = view.findViewById(R.id.timeText)
        val btnRemoveInstruction: ImageButton = view.findViewById(R.id.btnRemoveInstruction)

        init {
            btnRemoveInstruction.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val instruction = instructionList[position]
                    clickListener.onRemoveItemClick(it, instruction)
                }
            }
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val instruction = instructionList[position]
                    clickListener.onCListItemClick(it, instruction)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.instruction_plan_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val instruction = instructionList[position]
        holder.textViewAction.text = "Acción: ${instruction.instruction_description}"
        holder.textViewTime.text = "Tiempo: ${instruction.instruction_time} minutos"
    }

    override fun getItemCount(): Int {
        return instructionList.size
    }
}
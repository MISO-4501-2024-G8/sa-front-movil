package com.miso202402.SportApp.src.utils

import android.annotation.SuppressLint
import android.app.AlertDialog
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
import com.miso202402.SportApp.fragments.AddTrainingPlanFragment
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
        val objective = objectiveList.firstOrNull { it.day == instruction.id_objective }
        Log.i("ObjectiveAdapter","onRemoveItemClick")

        objective?.let { obj ->
            obj.instructions?.let { instructions ->
                Log.i("ObjectiveAdapter","Objective Day ${objective.day}")
                val position = instructions.indexOf(instruction)
                if (position != -1) {
                    obj.instructions = instructions.toMutableList().apply { removeAt(position) }
                    notifyDataSetChanged() // Notificar al adaptador del objetivo que los datos han cambiado
                    Log.i("ObjectiveAdapter","item removed")
                } else {
                    Log.e("ObjectiveAdapter", "Instrucción no encontrada en la lista")
                }
            }
        }
    }
    /*
    override fun onRemoveItemClick(view: View, instruction: Instruction){
        val objectiveID: String = instruction.id_objective.toString()
        val objective = objectiveList.first() {item ->
            item.day == objectiveID
        }

        instructionList = objective.instructions?.toMutableList() ?: mutableListOf()
        Log.i("ObjectiveAdapter","onRemoveItemClick")
        Log.i("ObjectiveAdapter","Objective Day ${objective.day}")
        val position = instructionList.indexOf(instruction)
        if (position != -1) {
            //instructionList.removeAt(position)
            objective.instructions?.toMutableList()?.apply {
                removeAt(position)
                objective.instructions = this
            }
            instructionAdapter.notifyDataSetChanged()
            Log.i("ObjectiveAdapter","onRemoveItemClick removed")
        } else {
            Log.e("InstructionAdapter", "Instrucción no encontrada en la lista")
        }
    }
    */

    private fun showAddInstructionDialog(instruction: Instruction) {
        val dialog = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.dialog_view_instruction, null)
        val textViewDescription = dialogView.findViewById<TextView>(R.id.textViewDescription)
        val textViewTime = dialogView.findViewById<TextView>(R.id.textViewTime)

        textViewDescription.text = instruction.instruction_description
        textViewTime.text = instruction.instruction_time.toString() + " minutos"

        dialog.setView(dialogView)
            .setTitle("Detalle de Instrucción")
            .setPositiveButton("Aceptar") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .create()
            .show()
    }

    override fun onCListItemClick(view: View, instruction: Instruction) {
        Log.i("ObjectiveAdapter", "onCListItemClick")
        showAddInstructionDialog(instruction)
    }

}
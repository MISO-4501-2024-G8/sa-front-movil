package com.miso202402.SportApp.fragments

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.miso202402.SportApp.src.models.request.InstructionTrainingPlanRequest
import com.miso202402.SportApp.src.models.request.ObjetiveTrainingPlanRequest
import com.miso202402.SportApp.src.models.request.TrainingPlanRequest
import com.miso202402.SportApp.src.models.response.InstructionTrainingPlansResponse
import com.miso202402.SportApp.src.models.response.ObjetiveTrainingPlanResponse
import com.miso202402.front_miso_pf2_g8_sportapp.R
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentAddTrainingPlanBinding
import com.miso202402.front_miso_pf2_g8_sportapp.src.models.response.TrainingPlansResponse
import com.miso202402.front_miso_pf2_g8_sportapp.src.services.ApiService
import com.miso202402.front_miso_pf2_g8_sportapp.src.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Vector


class AddTrainingPlanFragment : Fragment() {

    private var _binding: FragmentAddTrainingPlanBinding? = null
    private val binding get() = _binding!!

    var domain : String = "http://lb-ms-py-training-mngr-157212315.us-east-1.elb.amazonaws.com/"
    private var token: String? = ""
    private var id: String? = ""
    private var sport: String? = ""

    private var editTextInputInstructionDescription :String? = ""
    private var editTextInputInstructionTime :String? = ""

    private var instructions = arrayOf("","","","","")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTrainingPlanBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString("token")?.let {
            this.token = it.toString()
            Log.i("token", this.token.toString())
        }
        arguments?.getString("id")?.let {
            this.id = it.toString()
            Log.i("id", this.id.toString())
        }

        arguments?.getString("sport")?.let {
            this.sport = it.toString()
            Log.i("sport", this.id.toString())
        }

        val nameEditText = view.findViewById<EditText>(R.id.editTexName_FragmentAddTrainingPlan)
        val weeksEditText = view.findViewById<EditText>(R.id.editTexWeeks_FragmentAddTrainingPlan)
        val descriptionEditText = view.findViewById<EditText>(R.id.editTexDescription_FragmentAddTrainingPlan)

        var buttonAddPlan = view.findViewById<Button>(R.id.buttonAgregar_FragmentAddTrainingPlan)


        val numberPickerLunes = view.findViewById<NumberPicker>(R.id.numberPicker_lunes)
        numberPickerLunes.minValue = 1
        numberPickerLunes.maxValue = 10
        numberPickerLunes.isEnabled = false


        val numberPickerMartes = view.findViewById<NumberPicker>(R.id.numberPicker_martes)
        numberPickerMartes.minValue = 1
        numberPickerMartes.maxValue = 10
        numberPickerMartes.isEnabled = false

        val numberPickerMiercoles = view.findViewById<NumberPicker>(R.id.numberPicker_miercoles)
        numberPickerMiercoles.minValue = 1
        numberPickerMiercoles.maxValue = 10
        numberPickerMiercoles.isEnabled = false

        val numberPickerJueves = view.findViewById<NumberPicker>(R.id.numberPicker_jueves)
        numberPickerJueves.minValue = 1
        numberPickerJueves.maxValue = 10
        numberPickerJueves.isEnabled = false

        val numberPickerViernes = view.findViewById<NumberPicker>(R.id.numberPicker_viernes)
        numberPickerViernes.minValue = 1
        numberPickerViernes.maxValue = 10
        numberPickerViernes.isEnabled = false
        //numberPicker.setOnValueChangedListener{ picker, oldVal, newVal ->
        //}

        val checkBoxLunes = view.findViewById<CheckBox>(R.id.checkBox_lunes_FragmentAddTrainingPlan)
        val checkBoxMartes = view.findViewById<CheckBox>(R.id.checkBox_martes_FragmentAddTrainingPlan)
        val checkBoxMiercoles = view.findViewById<CheckBox>(R.id.checkBox_miercoles_FragmentAddTrainingPlan)
        val checkBoxJueves = view.findViewById<CheckBox>(R.id.checkBox_jueves_FragmentAddTrainingPlan)
        val checkBoxViernes = view.findViewById<CheckBox>(R.id.checkBox_viernes_FragmentAddTrainingPlan)

        checkBoxLunes.setOnClickListener {
            if( checkBoxLunes.isChecked)
                numberPickerLunes.isEnabled = true
            else numberPickerLunes.isEnabled = false
            ShowMeessageDialogDescription(0)
        }
        checkBoxMartes.setOnClickListener {
            if( checkBoxMartes.isChecked)
                numberPickerMartes.isEnabled = true
            else numberPickerMartes.isEnabled = false
            ShowMeessageDialogDescription(1)
        }
        checkBoxMiercoles.setOnClickListener {
            if( checkBoxMiercoles.isChecked)
                numberPickerMiercoles.isEnabled = true
            else numberPickerMiercoles.isEnabled = false
            ShowMeessageDialogDescription(2)
        }
        checkBoxJueves.setOnClickListener {
            if( checkBoxJueves.isChecked)
                numberPickerJueves.isEnabled = true
            else numberPickerJueves.isEnabled = false
            ShowMeessageDialogDescription(3)
        }
        checkBoxViernes.setOnClickListener {
            if( checkBoxViernes.isChecked)
                numberPickerViernes.isEnabled = true
            else numberPickerViernes.isEnabled = false
            ShowMeessageDialogDescription(4)
        }

        buttonAddPlan.setOnClickListener {
            Log.i("vector", instructions?.size.toString())
            Log.i("vector", instructions[0].toString())
            Log.i("Entre", "entre al boton")
            val sport: String = if(this.sport.toString() == "") "Ciclismo" else "Atletismo"
            val utils = Utils()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    Log.i("Entre", "entre a la corrutina")
                    val callCreateTrainingplan = utils.getRetrofit(domain)
                        .create(ApiService::class.java)
                        .createTrainingPlan(TrainingPlanRequest(
                            nameEditText.text.toString(),
                            descriptionEditText.text.toString(),
                            weeksEditText.text.toString().toInt(),
                            if (checkBoxLunes.isChecked()) 1 else 0,
                            if (checkBoxMartes.isChecked()) 1 else 0,
                            if (checkBoxMiercoles.isChecked()) 1 else 0,
                            if (checkBoxJueves.isChecked()) 1 else 0,
                            if (checkBoxViernes.isChecked()) 1 else 0,
                            "1",
                            sport,
                            "",
                            "")).execute()
                    val createTrainingplanResponse = callCreateTrainingplan.body() as TrainingPlansResponse?
                    var dia: String
                    var objective_repeats: Int;
                    val type_objective: String = "1"
                        Log.i("message createTrainingplanResponse", createTrainingplanResponse?.message.toString())
                        if (createTrainingplanResponse?.code == 200) {
                            val id_training_plan: String = createTrainingplanResponse.trainingPlan?.id.toString()
                            Log.i("id_training_plan", id_training_plan)

                            if (checkBoxLunes.isChecked() == true) {
                                dia = "Lunes"
                                objective_repeats = numberPickerLunes.value.toInt()
                                Log.i("objective_repeats", objective_repeats.toString())
                                makeObjetiveInstructions(dia, objective_repeats, type_objective, id_training_plan, utils)
                            }
                            if (checkBoxMartes.isChecked() == true) {
                                dia = "Martes"
                                objective_repeats = numberPickerLunes.value.toInt()
                                Log.i("objective_repeats", objective_repeats.toString())
                                makeObjetiveInstructions(dia, objective_repeats, type_objective, id_training_plan, utils)
                            }
                            if (checkBoxMiercoles.isChecked() == true) {
                                dia = "Miercoles"
                                Log.i("Entre miercoles", "Miercoles")
                                objective_repeats = numberPickerLunes.value.toInt()
                                Log.i("objective_repeats", objective_repeats.toString())
                                makeObjetiveInstructions(dia, objective_repeats, type_objective, id_training_plan, utils)
                            }
                            if (checkBoxJueves.isChecked() == true) {
                                dia = "Jueves"
                                objective_repeats = numberPickerLunes.value.toInt()
                                Log.i("objective_repeats", objective_repeats.toString())
                                makeObjetiveInstructions(dia, objective_repeats, type_objective, id_training_plan, utils)
                            }
                            if (checkBoxViernes.isChecked() == true) {
                                dia = "Viernes"
                                objective_repeats = numberPickerLunes.value.toInt()
                                Log.i("objective_repeats", objective_repeats.toString())
                                makeObjetiveInstructions(dia, objective_repeats, type_objective, id_training_plan, utils)
                            }
                            Log.i("Sali", "Sali de la De la creacion del Objeto")
                            val message: String = createTrainingplanResponse?.message.toString()
                            activity?.let { showMessageDialog(it, message) }


                        } else {
                            val errorMessage: String =
                                createTrainingplanResponse?.message.toString()
                            activity?.let { showMessageDialog(it, errorMessage) }
                        }
                    //}

                } catch (e: Exception) {
                    Log.e("error",e.message.toString())
                }
            }
        }


    }



    private fun makeObjetiveInstructions(
        dia: String,
        objective_repeats: Int,
        type_objective: String,
        id_training_plan:String,
        utils: Utils){

        Log.i("entre","Entre a la rutina de make Objetive")
        val callCreateObjetiveTrainingplan = utils.getRetrofit(domain)
            .create(ApiService::class.java)
            .createObjetiveTrainingPlan(ObjetiveTrainingPlanRequest(
                dia,
                objective_repeats,
                type_objective,
                id_training_plan,
            )).execute()
        val callCreateObjetiveTrainingplanResponse = callCreateObjetiveTrainingplan.body() as ObjetiveTrainingPlanResponse?
        Log.i("sALI Peticion rest Objetivo",callCreateObjetiveTrainingplanResponse?.code.toString())
        if(callCreateObjetiveTrainingplanResponse?.code == 200){
           Log.i("Sali Peticion rest Objetivo", callCreateObjetiveTrainingplanResponse?.objective?.id.toString())
            val idObjective = callCreateObjetiveTrainingplanResponse?.objective?.id.toString()
            var index = 0
            var instructionTrainingPlanRequest : InstructionTrainingPlanRequest? = null
            if(dia == "Lunes"){
                index = 0
                Log.i("Entre al rest Lunes", "Entre al rest de crear Instruccion")
                instructionTrainingPlanRequest =
                    InstructionTrainingPlanRequest(
                        instructions[index],
                        objective_repeats.toString(),
                        idObjective
                    )
                instructionTrainingPlanRequest?.instruction_description?.let {
                    Log.i("Entre al rest 3",
                        it
                    )
                }
                val callCreateInstructionTrainingplan =
                    utils.getRetrofit(domain)
                        .create(ApiService::class.java)
                        .createInstructionTrainingPlan(instructionTrainingPlanRequest)
                        .execute()
                callCreateInstructionTrainingplan.body() as InstructionTrainingPlansResponse?
                Log.i("Sali al rest", "Sali al rest de crear Instruccion")
                Log.i("Sali al rest, Instruction", callCreateInstructionTrainingplan.code().toString())
                callCreateInstructionTrainingplan.body()?.instruction?.id?.let {
                    Log.i("Sali al rest, Instruction",
                        it
                    )
                }
            }
            if(dia == "Martes"){
               index = 1
               Log.i("Entre al rest Martes", "Entre al rest de crear Instruccion")
               instructionTrainingPlanRequest =
                   InstructionTrainingPlanRequest(
                       instructions[index],
                       objective_repeats.toString(),
                       idObjective
                   )
               instructionTrainingPlanRequest?.instruction_description?.let {
                   Log.i("Entre al rest 3",
                       it
                   )
               }
               val callCreateInstructionTrainingplan =
                   utils.getRetrofit(domain)
                       .create(ApiService::class.java)
                       .createInstructionTrainingPlan(instructionTrainingPlanRequest)
                       .execute()
               callCreateInstructionTrainingplan.body() as InstructionTrainingPlansResponse?
               Log.i("Sali al rest", "Sali al rest de crear Instruccion")
               Log.i("Sali al rest, Instruction", callCreateInstructionTrainingplan.code().toString())
               callCreateInstructionTrainingplan.body()?.instruction?.id?.let {
                   Log.i("Sali al rest, Instruction",
                       it
                   )
               }
            }
            if(dia == "Mieroles"){
                index = 2
                Log.i("Entre al rest Miercoles", "Entre al rest de crear Instruccion")
                instructionTrainingPlanRequest =
                    InstructionTrainingPlanRequest(
                        instructions[index],
                        objective_repeats.toString(),
                        idObjective
                    )
                instructionTrainingPlanRequest?.instruction_description?.let {
                    Log.i("Entre al rest 3",
                        it
                    )
                }
                val callCreateInstructionTrainingplan =
                    utils.getRetrofit(domain)
                        .create(ApiService::class.java)
                        .createInstructionTrainingPlan(instructionTrainingPlanRequest)
                        .execute()
                callCreateInstructionTrainingplan.body() as InstructionTrainingPlansResponse?
                Log.i("Sali al rest", "Sali al rest de crear Instruccion")
                Log.i("Sali al rest, Instruction", callCreateInstructionTrainingplan.code().toString())
                callCreateInstructionTrainingplan.body()?.instruction?.id?.let {
                    Log.i("Sali al rest, Instruction",
                        it
                    )
                }
            }
            if(dia == "Jueves"){
                index = 3
                Log.i("Entre al rest Jueves", "Entre al rest de crear Instruccion")
                instructionTrainingPlanRequest =
                    InstructionTrainingPlanRequest(
                        instructions[index],
                        objective_repeats.toString(),
                        idObjective
                    )
                instructionTrainingPlanRequest?.instruction_description?.let {
                    Log.i("Entre al rest 3",
                        it
                    )
                }
                val callCreateInstructionTrainingplan =
                    utils.getRetrofit(domain)
                        .create(ApiService::class.java)
                        .createInstructionTrainingPlan(instructionTrainingPlanRequest)
                        .execute()
                callCreateInstructionTrainingplan.body() as InstructionTrainingPlansResponse?
                Log.i("Sali al rest", "Sali al rest de crear Instruccion")
                Log.i("Sali al rest, Instruction", callCreateInstructionTrainingplan.code().toString())
                callCreateInstructionTrainingplan.body()?.instruction?.id?.let {
                    Log.i("Sali al rest, Instruction",
                        it
                    )
                }
            }
            if(dia == "Viernes"){
                index = 4
                Log.i("Entre al rest Viernes", "Entre al rest de crear Instruccion")
                instructionTrainingPlanRequest =
                    InstructionTrainingPlanRequest(
                        instructions[index],
                        objective_repeats.toString(),
                        idObjective
                    )
                instructionTrainingPlanRequest?.instruction_description?.let {
                    Log.i("Entre al rest 3",
                        it
                    )
                }
                val callCreateInstructionTrainingplan =
                    utils.getRetrofit(domain)
                        .create(ApiService::class.java)
                        .createInstructionTrainingPlan(instructionTrainingPlanRequest)
                        .execute()
                callCreateInstructionTrainingplan.body() as InstructionTrainingPlansResponse?
                Log.i("Sali al rest", "Sali al rest de crear Instruccion")
                Log.i("Sali al rest, Instruction", callCreateInstructionTrainingplan.code().toString())
                callCreateInstructionTrainingplan.body()?.instruction?.id?.let {
                    Log.i("Sali al rest, Instruction",
                        it
                    )
                }
            } /**/
        }

    }

    fun showMessageDialog(activity: Activity, message: String) {
        lifecycleScope.launch {
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
        }
    }

    private fun ShowMeessageDialogDescription(index : Int) {
        lifecycleScope.launch {
            val inputEditTextField = EditText(requireActivity())
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("Instruccion")
                .setMessage("Agregue la descpricion de la instrucción")
                .setView(inputEditTextField)
                .setPositiveButton("OK") { _, _ ->
                    instructions[index] = inputEditTextField.text.toString()
                    Log.i("info vector index", index.toString())
                    Log.i("info vector respuesta", instructions[index])
                }
                .setNegativeButton("Cancel", null)
                .create()
            dialog.show()
        }
    }





}
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
import com.miso202402.SportApp.src.models.request.InstructionTrainingPlanRequest
import com.miso202402.SportApp.src.models.request.ObjetiveTrainingPlanRequest
import com.miso202402.SportApp.src.models.request.TrainingPlanRequest
import com.miso202402.front_miso_pf2_g8_sportapp.R
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentAddTrainingPlanBinding
import com.miso202402.front_miso_pf2_g8_sportapp.src.models.response.CreateTrainingPlansResponse
import com.miso202402.front_miso_pf2_g8_sportapp.src.services.ApiService
import com.miso202402.front_miso_pf2_g8_sportapp.src.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AddTrainingPlanFragment : Fragment() {

    private var _binding: FragmentAddTrainingPlanBinding? = null
    private val binding get() = _binding!!

    var domain : String = "http://lb-ms-py-training-mngr-157212315.us-east-1.elb.amazonaws.com/"
    var domain_2 : String = "http://lb-ms-py-training-mngr-157212315.us-east-1.elb.amazonaws.com/"
    private var token: String? = ""
    private var id: String? = ""
    private var sport: String? = ""

    private var editTextInputInstructionDescription :String? = ""
    private var editTextInputInstructionTime :String? = ""


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
        //buttonAddPlan.isEnabled = false

        val numberPickerLunes = view.findViewById<NumberPicker>(R.id.numberPicker_lunes)
        numberPickerLunes.minValue = 0
        numberPickerLunes.maxValue = 10

        val numberPickerMartes = view.findViewById<NumberPicker>(R.id.numberPicker_martes)
        numberPickerMartes.minValue = 0
        numberPickerMartes.maxValue = 10

        val numberPickerMiercoles = view.findViewById<NumberPicker>(R.id.numberPicker_miercoles)
        numberPickerMiercoles.minValue = 0
        numberPickerMiercoles.maxValue = 10

        val numberPickerJueves = view.findViewById<NumberPicker>(R.id.numberPicker_jueves)
        numberPickerJueves.minValue = 0
        numberPickerJueves.maxValue = 10

        val numberPickerViernes = view.findViewById<NumberPicker>(R.id.numberPicker_viernes)
        numberPickerViernes.minValue = 0
        numberPickerViernes.maxValue = 10
        //numberPicker.setOnValueChangedListener{ picker, oldVal, newVal ->
        //}

        val checkBoxLunes = view.findViewById<CheckBox>(R.id.checkBox_lunes_FragmentAddTrainingPlan)
        val checkBoxMartes = view.findViewById<CheckBox>(R.id.checkBox_martes_FragmentAddTrainingPlan)
        val checkBoxMiercoles = view.findViewById<CheckBox>(R.id.checkBox_miercoles_FragmentAddTrainingPlan)
        val checkBoxJueves = view.findViewById<CheckBox>(R.id.checkBox_jueves_FragmentAddTrainingPlan)
        val checkBoxViernes = view.findViewById<CheckBox>(R.id.checkBox_viernes_FragmentAddTrainingPlan)

        buttonAddPlan.setOnClickListener {
            Log.i("Entre", "entre al boton")
            val name: String = nameEditText.text.toString()
            val description: String = descriptionEditText.text.toString()
            val weeks: Int = weeksEditText.text.toString().toInt()
            val lunes: Int = if (checkBoxLunes.isChecked()) 1 else 0
            val martes: Int = if (checkBoxMartes.isChecked()) 1 else 0
            val miercoles: Int = if (checkBoxMiercoles.isChecked()) 1 else 0
            val jueves: Int = if (checkBoxJueves.isChecked()) 1 else 0
            val viernes: Int = if (checkBoxViernes.isChecked()) 1 else 0
            val sport: String = if(this.sport.toString() == "") "Ciclismo" else "Atletismo"
            val trainingPlanRequest = TrainingPlanRequest(name,
                description,
                weeks,
                lunes,
                martes,
                miercoles,
                jueves,
                viernes,
                "1",
                sport
            )
            val utils = Utils()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    Log.i("Entre", "entre a la corrutina")
                    val callCreateTrainingplan = utils.getRetrofit(domain).create(ApiService::class.java).createTrainingPlan(trainingPlanRequest).execute()
                    val createTrainingplanResponse = callCreateTrainingplan.body() as CreateTrainingPlansResponse?
                    Log.i("Sali", "Sali de la primera peticíon rest")

                    Log.i("Entre", "Entre a la segunda peticíon rest")
                    val objetiveTrainingPlanRequest = ObjetiveTrainingPlanRequest("lunes", 5,
                        "1",
                        createTrainingplanResponse?.trainingPlan?.id!!,
                        null
                    )
                    val callCreateObjetiveTrainingplan = utils.getRetrofit(domain_2).create(ApiService::class.java).createObjetiveTrainingPlan(objetiveTrainingPlanRequest).execute()
                    val callCreateObjetiveTrainingplanResponse = callCreateObjetiveTrainingplan.body()
                    Log.i("Sali", "Sali de la segunda peticíon rest")

                    lifecycleScope.launch {

                        var dia: String
                        var objective_repeats: Int;
                        val type_objective: String = "1"
                        if (createTrainingplanResponse?.message == "Se pudo crear la sesión de entrenamiento exitosamante") {
                            //Log.i("message", createTrainingplanResponse?.message!!)
                            //Log.i("id", createTrainingplanResponse.trainingPlan!!.id)

                            val id_training_plan: String = createTrainingplanResponse.trainingPlan?.id.toString()
                            Log.i("id_training_plan", id_training_plan)





                            Log.i("Objetive",
                                callCreateObjetiveTrainingplanResponse?.objective?.id!!
                            )

                           /* if (lunes == 1) {

                                dia = "Lunes"
                                Log.i("dia", dia)
                                objective_repeats = numberPickerLunes.value.toInt()
                                Log.i("objective_repeats", objective_repeats.toString())
                                makeObjetiveInstructions(dia, objective_repeats, type_objective, id_training_plan, utils)
                            }

                            if (martes == 1) {
                                dia = "Martes".toString()
                                Log.i("dia", dia)
                                objective_repeats = numberPickerMartes.value.toInt()
                                Log.i("objective_repeats", objective_repeats.toString())
                                makeObjetiveInstructions(
                                    dia,
                                    objective_repeats,
                                    type_objective,
                                    id_training_plan,
                                    utils
                                )
                            }

                            if (miercoles == 1) {
                                dia = "Miercoles".toString()
                                Log.i("dia", dia)
                                objective_repeats = numberPickerMiercoles.value.toInt()
                                makeObjetiveInstructions(
                                    dia,
                                    objective_repeats,
                                    type_objective,
                                    id_training_plan,
                                    utils
                                )
                            }

                            if (jueves == 1) {
                                dia = "Jueves".toString()
                                Log.i("dia", dia)
                                objective_repeats = numberPickerJueves.value.toInt()
                                makeObjetiveInstructions(
                                    dia,
                                    objective_repeats,
                                    type_objective,
                                    id_training_plan,
                                    utils
                                )
                            }

                            if (viernes == 1) {
                                dia = "Viernes".toString()
                                Log.i("dia", dia)
                                objective_repeats = numberPickerViernes.value.toInt()
                                makeObjetiveInstructions(
                                    dia,
                                    objective_repeats,
                                    type_objective,
                                    id_training_plan,
                                    utils
                                )
                            }*/

                            // Se pudo crear el plan de entrenamiento
                            val message: String = createTrainingplanResponse?.message.toString()
                            activity?.let { showMessageDialog(it, message) }
                        } else {
                            // No se pudo crear el plan de entrenamiento
                            val errorMessage: String =
                                createTrainingplanResponse?.message.toString()
                            activity?.let { showMessageDialog(it, errorMessage) }
                        }
                    }

                } catch (e: Exception) {
                    Log.e("error",e.message.toString())
                }
            }
        }


    }

    private fun ShowMeessageDialogDescription() {
        val inputEditTextField = EditText(requireActivity())
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Instruccion")
            .setMessage("Agregue la descpricion de la intrucción")
            .setView(inputEditTextField)
            .setPositiveButton("OK") { _, _ ->
                this.editTextInputInstructionDescription = inputEditTextField.text.toString()
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }

    private fun ShowMeessageDialogTime() {
        val inputEditTextField = EditText(requireActivity())
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Instruccion")
            .setMessage("Agregue el tiempo de la intrucción")
            .setView(inputEditTextField)
            .setPositiveButton("OK") { _, _ ->
                this.editTextInputInstructionTime = inputEditTextField.text.toString()
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }

    private fun makeObjetiveInstructions(dia: String, objective_repeats: Int, type_objective: String, id_training_plan:String, utils: Utils){
        Log.i("entre","Entre a la rutina de make Objetive")
        val objetiveTrainingPlanRequest = ObjetiveTrainingPlanRequest(dia, objective_repeats,
            type_objective,
            id_training_plan,
            null
        )
        val callCreateObjetiveTrainingplan = utils.getRetrofit(domain).create(ApiService::class.java).createObjetiveTrainingPlan(objetiveTrainingPlanRequest).execute()
        val callCreateObjetiveTrainingplanResponse = callCreateObjetiveTrainingplan.body()

        val idObjective = callCreateObjetiveTrainingplanResponse?.objective?.id.toString()
        for(i in 1 ..objective_repeats){
            ShowMeessageDialogDescription()
            ShowMeessageDialogTime()
            val instructionTrainingPlanRequest = InstructionTrainingPlanRequest(
                editTextInputInstructionDescription.toString(),
                editTextInputInstructionTime.toString(),
                idObjective)
            val callCreateInstructionTrainingplan = utils.getRetrofit(domain).create(ApiService::class.java).createInstructionTrainingPlan(instructionTrainingPlanRequest).execute()
            val  callCreateInstructionTrainingplanRequest= callCreateInstructionTrainingplan.body()
        }
    }

    fun showMessageDialog(activity: Activity, message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }





}
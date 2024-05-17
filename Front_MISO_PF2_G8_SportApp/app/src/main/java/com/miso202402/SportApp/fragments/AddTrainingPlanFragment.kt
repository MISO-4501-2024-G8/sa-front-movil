package com.miso202402.SportApp.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.addCallback
import androidx.annotation.RequiresExtension
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.miso202402.SportApp.src.models.models.Instruction
import com.miso202402.SportApp.src.models.models.Objective
import com.miso202402.SportApp.src.models.models.TrainingPlan
import com.miso202402.SportApp.src.models.request.InstructionTrainingPlanRequest
import com.miso202402.SportApp.src.models.request.ObjetiveTrainingPlanRequest
import com.miso202402.SportApp.src.models.request.RiskAlertsTrainingPlanRequest
import com.miso202402.SportApp.src.models.request.TrainingPlanRequest
import com.miso202402.SportApp.src.models.response.InstructionTrainingPlansResponse
import com.miso202402.SportApp.src.models.response.ObjetiveTrainingPlanResponse
import com.miso202402.SportApp.src.models.response.RiskTrainingPlanResponse
import com.miso202402.SportApp.src.utils.ClickListener_Objective
import com.miso202402.SportApp.src.utils.ObjectiveAdapter
import com.miso202402.SportApp.src.utils.SharedPreferences
import com.miso202402.front_miso_pf2_g8_sportapp.R
import com.miso202402.front_miso_pf2_g8_sportapp.activities.MainActivity
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.FragmentAddTrainingPlanBinding
import com.miso202402.front_miso_pf2_g8_sportapp.src.models.response.TrainingPlansResponse
import com.miso202402.front_miso_pf2_g8_sportapp.src.services.ApiService
import com.miso202402.front_miso_pf2_g8_sportapp.src.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AddTrainingPlanFragment : Fragment(), ClickListener_Objective {

    private var _binding: FragmentAddTrainingPlanBinding? = null
    private val binding get() = _binding!!

    var domain : String = "http://lb-ms-py-training-mngr-157212315.us-east-1.elb.amazonaws.com/"
    private var user_id: String? = ""
    private var typePlan: String? = ""
    private var FoodRoutineId: String? = ""
    private var RestRoutineId: String? = ""
    private var instructions = arrayOf("","","","","")
    private lateinit var preferences: SharedPreferences
    private var tipoDeporte : String? = "Atletismo"
    private var vectorTipoDeporte  =  arrayOf("Atletismo", "Ciclismo")
    private lateinit var nameEditText: EditText
    private lateinit var weeksEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var buttonAddPlan: Button
    private lateinit var buttonAtras: Button
    private lateinit var buttonSeleccionarFoodR: Button
    private lateinit var buttonSeleccionarRestR: Button
    private lateinit var buttonSeleccionarAlerts: Button

    lateinit var objectiveAdapter: ObjectiveAdapter
    lateinit var listener: ClickListener_Objective
    lateinit var objectiveList: MutableList<Objective>
    lateinit var baseObjectiveList: MutableList<Objective>

    private var stop_training: Boolean = false;
    private var notification_msg: Boolean = false;
    private var emergency_call: Boolean = false;
    private var alertasE: String = ""


    override fun onAttach(context: Context) {
        super.onAttach(context)
        preferences = SharedPreferences(requireContext())
    }


    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTrainingPlanBinding.inflate(inflater, container, false)
        user_id = preferences.getData<String>("id").toString()
        typePlan = preferences.getData<String>("typePlan").toString()
        alertasE = preferences.getData<String>("alertasE").toString()
        Log.i("user_id", user_id!!)
        Log.i("typePlan", typePlan!!)
        Log.i("alertasE", alertasE!!)
        listener = this
        objectiveList =  mutableListOf()
        baseObjectiveList = mutableListOf()
        objectiveAdapter = context?.let { ObjectiveAdapter(objectiveList, it, listener) }!!
        binding.recyclerviewListObjetivos.setHasFixedSize(true)
        binding.recyclerviewListObjetivos.layoutManager = LinearLayoutManager(context)
        binding.recyclerviewListObjetivos.adapter = objectiveAdapter

        val objectivoLunes = Objective("","","Lunes",0,"1", listOf())
        baseObjectiveList.add(objectivoLunes)
        objectiveList.add(objectivoLunes)
        val objectivoMartes = Objective("","","Martes",0,"1", listOf())
        baseObjectiveList.add(objectivoMartes)
        objectiveList.add(objectivoMartes)
        val objectivoMiercoles = Objective("","","Miercoles",0,"1", listOf())
        baseObjectiveList.add(objectivoMiercoles)
        objectiveList.add(objectivoMiercoles)
        val objectivoJueves = Objective("","","Jueves",0,"1", listOf())
        baseObjectiveList.add(objectivoJueves)
        objectiveList.add(objectivoJueves)
        val objectivoViernes = Objective("","","Viernes",0,"1", listOf())
        baseObjectiveList.add(objectivoViernes)
        objectiveList.add(objectivoViernes)
        objectiveAdapter.notifyDataSetChanged()

        stop_training = arguments?.getBoolean("stop_training") == true
        notification_msg = arguments?.getBoolean("notification_msg") == true
        emergency_call = arguments?.getBoolean("emergency_call") == true

        return binding.root
    }

    override fun onAddItemClick(view: View, objective: Objective) {
        // Crear un cuadro de diálogo para agregar una nueva instrucción
        val dialog = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_add_instruction, null)
        val editTextDescription = dialogView.findViewById<EditText>(R.id.editTextDescription)
        val editTextTime = dialogView.findViewById<EditText>(R.id.editTextTime)

        dialog.setView(dialogView)
            .setTitle("Agregar Instrucción")
            .setPositiveButton("Agregar") { dialogInterface, _ ->
                val description = editTextDescription.text.toString()
                val time = editTextTime.text.toString().toIntOrNull() ?: 0
                if (description.isNotEmpty() && time > 0) {
                    val newInstruction = Instruction("",objective.day,description, time)
                    objective.instructions?.toMutableList()?.apply {
                        add(newInstruction)
                        objective.instructions = this
                    }
                    notifyDataSetChanged()
                } else {
                    Toast.makeText(requireContext(), "Por favor, ingrese una descripción y un tiempo válido.", Toast.LENGTH_SHORT).show()
                }
                dialogInterface.dismiss()
            }
            .setNegativeButton("Cancelar") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .create()
            .show()
    }

    fun notifyDataSetChanged(){
        objectiveAdapter.notifyDataSetChanged()
    }

    override fun onRemoveItemClick(view: View, instruction: Instruction) {
        Log.i("AddTrainingPlanFragment", "onRemoveItemClick")
        // Aquí puedes implementar la lógica para eliminar la instrucción del adaptador de objetivos
        objectiveAdapter.onRemoveItemClick(view, instruction)
        objectiveAdapter.notifyDataSetChanged() // Notificar al adaptador del objetivo que los datos han cambiado
    }


    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                mostrarSnackbar("Utilizar los botones de la aplicacion para navegar.")
                return@setOnKeyListener true
            }
            false
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            mostrarSnackbar("Utiliza los botones de la aplicación para navegar.")
        }

        var spinner = view.findViewById<Spinner>(R.id.spinner_TrainingSessionFragment)
        activity?.let {
            ArrayAdapter.createFromResource(it,
                R.array.SportPlanCreation,
                android.R.layout.simple_spinner_item
            ).also { arrayAdapter ->
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = arrayAdapter
            }
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                tipoDeporte = vectorTipoDeporte[p2]
                Log.i("mesnaje al selecionar tipo de deporte ", tipoDeporte.toString())
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        var typePlan: String = preferences.getData<String>("typePlan").toString()
        nameEditText = view.findViewById<EditText>(R.id.editTexName_FragmentAddTrainingPlan)
        weeksEditText = view.findViewById<EditText>(R.id.editTexWeeks_FragmentAddTrainingPlan)
        descriptionEditText = view.findViewById<EditText>(R.id.editTexDescription_FragmentAddTrainingPlan)
        buttonAddPlan = view.findViewById<Button>(R.id.buttonAgregar_FragmentAddTrainingPlan)
        buttonAtras = view.findViewById<Button>(R.id.buttonAtras_FragmentAddTrainingPlan)
        buttonSeleccionarFoodR = view.findViewById<Button>(R.id.buttonAlimentacion_FragmentAddTrainingPlan)
        buttonSeleccionarRestR = view.findViewById<Button>(R.id.buttonDescanso_FragmentAddTrainingPlan)
        buttonSeleccionarAlerts = view.findViewById<Button>(R.id.buttonAlertas_FragmentAddTrainingPlan)
        if(typePlan != "basico"){
            buttonSeleccionarAlerts.visibility = View.VISIBLE
        }else{
            buttonSeleccionarAlerts.visibility = View.GONE
        }

        buttonSeleccionarAlerts.setOnClickListener(){
            var objectivesTemp : MutableList<Objective> = this.objectiveList
            saveTempTrainingPlan(objectivesTemp)
            val bundle = bundleOf(
                "stop_training" to stop_training,
                "notification_msg" to notification_msg,
                "emergency_call" to emergency_call
            )
            val mainActivity = requireActivity() as? MainActivity
            mainActivity?.navigateToFragment(R.id.TrainingPlanAlertsFragment, "Alertas", bundle)
        }

        buttonAtras.setOnClickListener(){
            val mainActivity = requireActivity() as? MainActivity
            mainActivity?.navigateToFragment(R.id.trainingSessionFragment, "Plan de Entrenamiento", null)
        }

        buttonSeleccionarFoodR.setOnClickListener(){
            var objectivesTemp : MutableList<Objective> = this.objectiveList
            saveTempTrainingPlan(objectivesTemp)
            val mainActivity = requireActivity() as? MainActivity
            mainActivity?.navigateToFragment(R.id.FoodRoutineListFragment, "Rutina de Alimentacion", null)
        }

        buttonSeleccionarRestR.setOnClickListener(){
            var objectivesTemp : MutableList<Objective> = this.objectiveList
            saveTempTrainingPlan(objectivesTemp)
            val mainActivity = requireActivity() as? MainActivity
            mainActivity?.navigateToFragment(R.id.RestRoutineListFragment, "Rutina de Descanso", null)
        }
        buttonAddPlan.setOnClickListener(){

            if(nameEditText.text.toString() == ""){
                mostrarSnackbar("El nombre del plan no debe ser vacio")
                return@setOnClickListener
            }
            if(weeksEditText.text.toString() == ""){
                mostrarSnackbar("La cantidad de semanas del plan no debe ser vacia")
                return@setOnClickListener
            }
            if(descriptionEditText.text.toString() == ""){
                mostrarSnackbar("La descripcion del plan no debe ser vacio")
                return@setOnClickListener
            }
            for (objective in objectiveList) {
                Log.i("ObjectiveList", "Day: ${objective.day}, Checked: ${objective.checked},Repeats: ${objective.repeats}, Qty Instrucciones: ${objective.instructions?.size.toString()}")
                if(objective.checked == true){
                    if(objective.repeats == 0){
                        mostrarSnackbar("Las cantidad de repeticiones del dia ${objective.day} deben ser al menos 1")
                        return@setOnClickListener
                    }
                    if(objective.instructions?.size == 0){
                        mostrarSnackbar("Las cantidad de instrucciones del dia ${objective.day} deben ser al menos 1")
                        return@setOnClickListener
                    }
                }
            }
            if(FoodRoutineId.toString() == ""){
                mostrarSnackbar("Se debe seleccionar al menos una Rutina de Alimentacion")
                return@setOnClickListener
            }

            if(RestRoutineId.toString() == ""){
                mostrarSnackbar("Se debe seleccionar al menos una Rutina de Descanso")
                return@setOnClickListener
            }

            if(typePlan != "basico" && (alertasE == "null" || alertasE == "")){
                mostrarSnackbar("Se deben seleccionar las alertas")
                return@setOnClickListener
            }
            var objectivesTemp : MutableList<Objective> = this.objectiveList
            AddTrainingPlan(objectivesTemp)
            Log.i("ObjectiveList","Continuar Proceso de Creacion de Plan..")
            Log.i("ObjectiveList","Nombre: ${nameEditText.text} Semanas: ${weeksEditText.text} Descripcion: ${descriptionEditText.text} Deporte: $tipoDeporte")
        }
        createTempTrainingPlan(baseObjectiveList)
    }

    private fun saveTempTrainingPlan(objectivesTemp:MutableList<Objective>){
        var planName:String = nameEditText.text.toString()
        var planDescription:String = descriptionEditText.text.toString()
        var planWeeks:String = weeksEditText.text.toString()
        var lunes_enabled:Int = 0
        var martes_enabled:Int = 0
        var miercoles_enabled:Int = 0
        var jueves_enabled:Int = 0
        var viernes_enabled:Int = 0
        var planType:String = typePlan.toString()
        var planSport:String = tipoDeporte.toString()
        var id_eating_routine:String = FoodRoutineId.toString()
        var id_rest_routine:String = RestRoutineId.toString()
        var planObjectives:MutableList<Objective> = mutableListOf<Objective>().apply {
            addAll(objectivesTemp)
        }
        var tempTrainingPlan:TrainingPlan = TrainingPlan(
            "",
            planName,
            planDescription,
            planWeeks,
            lunes_enabled,
            martes_enabled,
            miercoles_enabled,
            jueves_enabled,
            viernes_enabled,
            planType,
            planSport,
            id_eating_routine,
            id_rest_routine,
            planObjectives
        )
        //context?.let { PreferenceHelper.saveTrainingPlan(it, tempTrainingPlan) }
        preferences.saveData("tempTrainingPlan", tempTrainingPlan)
    }

    private fun createTempTrainingPlan(baseObjectives:MutableList<Objective>){
        var tempTrainingPlan:TrainingPlan? = preferences.getData<TrainingPlan>("tempTrainingPlan")
        if(tempTrainingPlan != null){
            //var tempTrainingPlan_:TrainingPlan? = context?.let { PreferenceHelper.getTrainingPlan(it) }
            nameEditText.setText(tempTrainingPlan.name)
            descriptionEditText.setText(tempTrainingPlan.description)
            weeksEditText.setText(tempTrainingPlan.weeks)
            //objectiveList = tempTrainingPlan.objectives?.toMutableList() ?: baseObjectives
            FoodRoutineId = tempTrainingPlan.id_eating_routine
            if(FoodRoutineId != ""){
                buttonSeleccionarFoodR.setText("Cambiar Rutina de Alimentacion")
                context?.let { ContextCompat.getColor(it, R.color.correctBTN) }
                    ?.let { buttonSeleccionarFoodR.setBackgroundColor(it) }
            }
            RestRoutineId = tempTrainingPlan.id_rest_routine
            if(RestRoutineId != ""){
                buttonSeleccionarRestR.setText("Cambiar Rutina de Descanso")
                context?.let { ContextCompat.getColor(it, R.color.correctBTN) }
                    ?.let { buttonSeleccionarRestR.setBackgroundColor(it) }
            }
            if(alertasE != "null" && alertasE != ""){
                buttonSeleccionarAlerts.setText("Cambiar Alertas")
                context?.let { ContextCompat.getColor(it, R.color.correctBTN) }
                    ?.let { buttonSeleccionarAlerts.setBackgroundColor(it) }
            }
            tipoDeporte = tempTrainingPlan.sport

            objectiveList = mutableListOf<Objective>().apply {
                addAll(tempTrainingPlan.objectives?.toMutableList() ?: baseObjectives)
            }
            objectiveAdapter = context?.let { ObjectiveAdapter(objectiveList, it, listener) }!!
            binding.recyclerviewListObjetivos.setHasFixedSize(true)
            binding.recyclerviewListObjetivos.layoutManager = LinearLayoutManager(context)
            binding.recyclerviewListObjetivos.adapter = objectiveAdapter

            var spinner = view?.findViewById<Spinner>(R.id.spinner_TrainingSessionFragment)
            if (spinner != null) {
                val posicion = vectorTipoDeporte.indexOf(tipoDeporte)
                spinner.setSelection(posicion)
            }
            objectiveAdapter.notifyDataSetChanged()
        }
    }

    private fun mostrarSnackbar(mensaje: String) {
        view?.let {
            Snackbar.make(it, mensaje, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun checkDayEnabled(planObjectives:MutableList<Objective>, day:String):Int{
        val CheckedForDay: Boolean? = planObjectives
            .firstOrNull { it.day == day }
            ?.checked
        var enabled = if (CheckedForDay == true) 1 else 0
        return enabled
    }

    private fun AddTrainingPlan(objectivesTemp:MutableList<Objective>){
        mostrarSnackbar("Creando Plan de entrenamiento...")
        var planName:String = nameEditText.text.toString()
        var planDescription:String = descriptionEditText.text.toString()
        var planWeeks:String = weeksEditText.text.toString()
        var lunes_enabled:Int = 0
        var martes_enabled:Int = 0
        var miercoles_enabled:Int = 0
        var jueves_enabled:Int = 0
        var viernes_enabled:Int = 0
        var planType:String = typePlan.toString()
        var planSport:String = tipoDeporte.toString()
        var id_eating_routine:String = FoodRoutineId.toString()
        var id_rest_routine:String = RestRoutineId.toString()
        var planObjectives:MutableList<Objective> = mutableListOf<Objective>().apply {
            addAll(objectivesTemp)
        }
        // Revisar los dias que estan checkeados
        lunes_enabled = checkDayEnabled(planObjectives,"Lunes")
        martes_enabled = checkDayEnabled(planObjectives,"Martes")
        miercoles_enabled = checkDayEnabled(planObjectives,"Miercoles")
        jueves_enabled = checkDayEnabled(planObjectives,"Jueves")
        viernes_enabled = checkDayEnabled(planObjectives,"Viernes")
        // Filtrar solo los dias que estan checkeados
        planObjectives = planObjectives.filter { it.checked == true }.toMutableList()

        var tempTrainingPlan:TrainingPlan = TrainingPlan(
            "",
            planName,
            planDescription,
            planWeeks,
            lunes_enabled,
            martes_enabled,
            miercoles_enabled,
            jueves_enabled,
            viernes_enabled,
            planType,
            planSport,
            id_eating_routine,
            id_rest_routine,
            planObjectives
        )


        var idTrainingPlan = ""
        val utils = Utils()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.i("AddTrainingPlan", "Antes de creacion de plan")
                val callCreateTrainingPlan = utils.getRetrofit(domain)
                    .create(ApiService::class.java)
                    .createTrainingPlan(TrainingPlanRequest(
                        tempTrainingPlan.name.toString(),
                        tempTrainingPlan.description.toString(),
                        tempTrainingPlan.weeks.toString().toInt(),
                        tempTrainingPlan.lunes_enabled.toString().toInt(),
                        tempTrainingPlan.martes_enabled.toString().toInt(),
                        tempTrainingPlan.miercoles_enabled.toString().toInt(),
                        tempTrainingPlan.jueves_enabled.toString().toInt(),
                        tempTrainingPlan.viernes_enabled.toString().toInt(),
                        tempTrainingPlan.typePlan.toString(),
                        tempTrainingPlan.sport.toString(),
                        tempTrainingPlan.id_rest_routine.toString(),
                        tempTrainingPlan.id_eating_routine.toString()
                    )).execute()
                val createTrainingPlanResponse = callCreateTrainingPlan.body() as TrainingPlansResponse?
                Log.i("message createTrainingPlanResponse", createTrainingPlanResponse?.message.toString())
                if (createTrainingPlanResponse?.code == 200) {
                    idTrainingPlan = createTrainingPlanResponse.trainingPlan?.id.toString()
                    Log.i("id_training_plan", idTrainingPlan)
                    for (objective in planObjectives) {
                        createObjective(idTrainingPlan, objective, utils)
                    }
                    if(typePlan != "basico"){
                        createRiskAlerts(idTrainingPlan, stop_training, notification_msg, emergency_call, utils)
                    }
                    Log.i("createTrainingPlanResponse", "Finalizo creacion de Plan")
                    //mostrarSnackbar("El plan de entrenamiento fue creado satisfactoriamente")
                    //val mainActivity = requireActivity() as? MainActivity
                    //mainActivity?.navigateToFragment(R.id.trainingSessionFragment, "Plan de Entrenamiento")
                    val mainActivity = requireActivity() as? MainActivity
                    mainActivity?.runOnUiThread {
                        mostrarSnackbar("El plan de entrenamiento fue creado satisfactoriamente")
                        mainActivity.navigateToFragment(R.id.trainingSessionFragment, "Plan de Entrenamiento", null)
                    }

                }else{
                    var errorMessage = createTrainingPlanResponse?.message.toString()
                    Log.e("AddTrainingPlan error",errorMessage)
                    throw Exception("Error al crear el plan: $errorMessage")
                }
            }catch (e: Exception) {
                Log.e("AddTrainingPlan $idTrainingPlan error: ",e.message.toString())
                // revisar si existe un plan con el id creado, si si borrarlo, y enviar mensaje en pantalla del error
            }
        }

    }
    private fun createRiskAlerts(id_training_plan: String, stop_training: Boolean, notification_msg: Boolean, emergency_call: Boolean, utils: Utils){
        Log.i("createRiskAlerts","Antes de crear risk alerts")
        var stop_training_enabled = if (stop_training == true) 1 else 0
        var notification_msg_enabled = if (notification_msg == true) 1 else 0
        var emergency_call_enabled = if (emergency_call == true) 1 else 0
        val callCreateRiskAlertTrainingplan = utils.getRetrofit(domain)
            .create(ApiService::class.java)
            .createRiskAlertsTrainingPlan(
                RiskAlertsTrainingPlanRequest(
                    stop_training_enabled.toString(),
                    notification_msg_enabled.toString(),
                    emergency_call_enabled.toString(),
                    id_training_plan,
                )
            ).execute()
        val callCreateRiskAlertsTrainingPlanResponse = callCreateRiskAlertTrainingplan.body() as RiskTrainingPlanResponse?
        Log.i("createRiskAlerts",callCreateRiskAlertsTrainingPlanResponse?.code.toString())
        if(callCreateRiskAlertsTrainingPlanResponse?.code == 200){
            val stop_training = callCreateRiskAlertsTrainingPlanResponse?.risk_alerts?.stop_training.toString()
            val notifications = callCreateRiskAlertsTrainingPlanResponse?.risk_alerts?.notifications.toString()
            val enable_phone = callCreateRiskAlertsTrainingPlanResponse?.risk_alerts?.enable_phone.toString()
            Log.i("createRiskAlerts","Alerts stop_training $stop_training notifications $notifications enable_phone $enable_phone")
        }else{
            var errorMessage = callCreateRiskAlertsTrainingPlanResponse?.message.toString()
            Log.e("createRiskAlerts error",errorMessage)
            throw Exception("Error al crear las alertas: $errorMessage")
        }
    }

    private fun createObjective(id_training_plan: String, objective: Objective, utils: Utils){
        Log.i("createObjective","Antes de crear objetivo")
        val callCreateObjetiveTrainingplan = utils.getRetrofit(domain)
            .create(ApiService::class.java)
            .createObjetiveTrainingPlan(ObjetiveTrainingPlanRequest(
                objective.day,
                objective.repeats,
                objective.type_objective,
                id_training_plan,
            )).execute()
        val callCreateObjetiveTrainingPlanResponse = callCreateObjetiveTrainingplan.body() as ObjetiveTrainingPlanResponse?
        Log.i("createObjective",callCreateObjetiveTrainingPlanResponse?.code.toString())
        if(callCreateObjetiveTrainingPlanResponse?.code == 200){
            val idObjective = callCreateObjetiveTrainingPlanResponse?.objective?.id.toString()
            Log.i("createObjective",idObjective)
            for(instruction in objective.instructions!!){
                createInstruction(idObjective, instruction,utils)
            }
        }else{
            var errorMessage = callCreateObjetiveTrainingPlanResponse?.message.toString()
            Log.e("createObjective error",errorMessage)
            throw Exception("Error al crear el objetivo: $errorMessage")
        }

    }

    private fun createInstruction(id_objective: String, instruction: Instruction, utils: Utils){
        val callCreateInstructionTrainingplan =
            utils.getRetrofit(domain)
                .create(ApiService::class.java)
                .createInstructionTrainingPlan( InstructionTrainingPlanRequest(
                    instruction.instruction_description.toString(),
                    instruction.instruction_time.toString(),
                    id_objective
                ))
                .execute()
        val callCreateInstructionTrainingPlanResponse = callCreateInstructionTrainingplan.body() as InstructionTrainingPlansResponse?
        Log.i("createInstruction",callCreateInstructionTrainingPlanResponse?.code.toString())
        if(callCreateInstructionTrainingPlanResponse?.code == 200){
            val idInstruction = callCreateInstructionTrainingPlanResponse?.instruction?.id.toString()
        }else{
            var errorMessage = callCreateInstructionTrainingPlanResponse?.message.toString()
            Log.e("createInstruction error", errorMessage)
            throw Exception("Error al crear la instruccion: $errorMessage")
        }
    }

/*
    private fun AddTrainingPlan2(){
        Log.i("vector", instructions?.size.toString())
        Log.i("vector", instructions[0].toString())
        Log.i("Entre", "entre al boton")
        val sport: String = if(this.tipoDeporte.toString() == "") "Ciclismo" else "Atletismo"
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

                    /*
                    val bundle = bundleOf("token" to  token,
                        "id" to id,
                        "sport" to sport)
                    findNavController().navigate(R.id.action_addTrainingPlanFragment_to_FirstFragment, bundle)
                    */


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
    */


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
package com.miso202402.front_miso_pf2_g8_sportapp.src.services

import com.miso202402.SportApp.src.models.request.InstructionTrainingPlanRequest
import com.miso202402.SportApp.src.models.request.ObjetiveTrainingPlanRequest
import com.miso202402.SportApp.src.models.request.TrainingPlanRequest
import com.miso202402.SportApp.src.models.response.InstructionTrainingPlansResponse
import com.miso202402.SportApp.src.models.response.ObjetiveTrainingPlansResponse
import com.miso202402.front_miso_pf2_g8_sportapp.src.models.request.LoginRequest
import com.miso202402.front_miso_pf2_g8_sportapp.src.models.response.CreateTrainingPlansResponse
import com.miso202402.front_miso_pf2_g8_sportapp.src.models.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {


    @POST("login/user")
    fun logIn(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("training_plan/456")
    fun createTrainingPlan(@Body trainingPlanRequest: TrainingPlanRequest): Call<CreateTrainingPlansResponse>

    @POST("objetive_training_plan/456")
    fun createObjetiveTrainingPlan(@Body objetiveTrainingPlanRequest: ObjetiveTrainingPlanRequest): Call<ObjetiveTrainingPlansResponse>

    @POST("instruction_training_plan/123")
    fun createInstructionTrainingPlan(@Body instructionTrainingPlanRequest: InstructionTrainingPlanRequest): Call<InstructionTrainingPlansResponse>

}
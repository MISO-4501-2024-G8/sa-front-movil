package com.miso202402.front_miso_pf2_g8_sportapp.src.services

import com.miso202402.SportApp.src.models.request.InstructionTrainingPlanRequest
import com.miso202402.SportApp.src.models.request.ObjetiveTrainingPlanRequest
import com.miso202402.SportApp.src.models.request.TrainingPlanRequest
import com.miso202402.SportApp.src.models.response.InstructionTrainingPlansResponse
import com.miso202402.SportApp.src.models.response.ObjetiveTrainingPlanResponse
import com.miso202402.front_miso_pf2_g8_sportapp.src.models.request.LoginRequest
import com.miso202402.front_miso_pf2_g8_sportapp.src.models.response.TrainingPlansResponse
import com.miso202402.front_miso_pf2_g8_sportapp.src.models.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {


    @POST("login/user")
    fun logIn(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("training_plan")
    fun createTrainingPlan(@Body request: TrainingPlanRequest): Call<TrainingPlansResponse>

    @POST("objetive_training_plan")
    fun createObjetiveTrainingPlan(@Body request: ObjetiveTrainingPlanRequest): Call<ObjetiveTrainingPlanResponse>

    @POST("instruction_training_plan")
    fun createInstructionTrainingPlan(@Body request: InstructionTrainingPlanRequest?): Call<InstructionTrainingPlansResponse>

}
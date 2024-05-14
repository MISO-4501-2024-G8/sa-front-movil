package com.miso202402.front_miso_pf2_g8_sportapp.src.services

import com.miso202402.SportApp.src.models.request.EventsRequest
import com.miso202402.SportApp.src.models.response.GetAllRutasResponse
import com.miso202402.SportApp.src.models.request.InstructionTrainingPlanRequest
import com.miso202402.SportApp.src.models.request.ObjetiveTrainingPlanRequest
import com.miso202402.SportApp.src.models.request.RoutsRequest
import com.miso202402.SportApp.src.models.request.TrainingPlanRequest
import com.miso202402.SportApp.src.models.request.TrainingSessionRequest
import com.miso202402.SportApp.src.models.response.GetAllEatingRoutineResponse
import com.miso202402.SportApp.src.models.response.GetAllEventsResponse
import com.miso202402.SportApp.src.models.response.GetAllRestRoutineResponse
import com.miso202402.SportApp.src.models.response.GetAllUserTrainingSessionsResponse
import com.miso202402.SportApp.src.models.response.InstructionTrainingPlansResponse
import com.miso202402.SportApp.src.models.response.ObjetiveTrainingPlanResponse
import com.miso202402.SportApp.src.models.response.GetEventResponse
import com.miso202402.SportApp.src.models.response.GetRoutsResponse
import com.miso202402.SportApp.src.models.response.TraingSessionResponse
import com.miso202402.SportApp.src.models.response.TrainingListPlansResponse
import com.miso202402.SportApp.src.models.response.ValidateTokenResponse
import com.miso202402.front_miso_pf2_g8_sportapp.src.models.request.LoginRequest
import com.miso202402.front_miso_pf2_g8_sportapp.src.models.response.TrainingPlansResponse
import com.miso202402.front_miso_pf2_g8_sportapp.src.models.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {


    @POST("login/user")
    fun logIn(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("training_plan")
    fun createTrainingPlan(@Body request: TrainingPlanRequest): Call<TrainingPlansResponse>

    @GET("training_plan")
    fun getTrainingPlans(): Call<TrainingListPlansResponse>

    @GET("eating_routing_training_plan")
    fun getAllEatingRoutines():Call<GetAllEatingRoutineResponse>

    @GET("rest_routine_training_plan")
    fun getAllRestRoutineResponse():Call<GetAllRestRoutineResponse>

    @POST("objetive_training_plan")
    fun createObjetiveTrainingPlan(@Body request: ObjetiveTrainingPlanRequest): Call<ObjetiveTrainingPlanResponse>

    @POST("instruction_training_plan")
    fun createInstructionTrainingPlan(@Body request: InstructionTrainingPlanRequest?): Call<InstructionTrainingPlansResponse>

    @GET("eventos")
    fun getAllEventos():Call<GetAllEventsResponse>

    @GET("eventos/{evento_id}")
    fun getEventoById(@Path("evento_id") evento_id: String):Call<GetEventResponse>

    @POST("eventos")
    fun createEventos(@Body request: EventsRequest):Call<GetEventResponse>

    @PUT("eventos/{evento_id}")
    fun updateEventoById(@Path("evento_id") evento_id: String, @Body request: EventsRequest): Call<GetEventResponse>

    @POST("training_session")
    fun createTrainigSession(@Body request: TrainingSessionRequest):Call<TraingSessionResponse>

    @GET("rutas")
    fun getAllRutas():Call<GetAllRutasResponse>

    @GET("rutas/{ruta_id}")
    fun getRutaById(@Path("ruta_id") ruta_id: String):Call<GetRoutsResponse>

    @POST("rutas")
    fun createRuta(@Body request: RoutsRequest):Call<GetRoutsResponse>

    @PUT("rutas")
    fun updateRoutById(@Path("ruta_id") ruta_id: String, @Body request: RoutsRequest): Call<GetRoutsResponse>

    @GET("training_session/{user_id}")
    fun getTrainingSessionsById(@Path("user_id") user_id: String):Call<GetAllUserTrainingSessionsResponse>

    @GET("login/validate_token")
    fun validateSession():Call<ValidateTokenResponse>

}
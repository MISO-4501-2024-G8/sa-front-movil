package com.miso202402.front_miso_pf2_g8_sportapp.src.services

import com.miso202402.SportApp.src.models.models.SportObjectiveSession
import com.miso202402.SportApp.src.models.request.ConsultationRequest
import com.miso202402.SportApp.src.models.request.EventsRequest
import com.miso202402.SportApp.src.models.response.GetAllRutasResponse
import com.miso202402.SportApp.src.models.request.InstructionTrainingPlanRequest
import com.miso202402.SportApp.src.models.request.ObjetiveTrainingPlanRequest
import com.miso202402.SportApp.src.models.request.RiskAlertsTrainingPlanRequest
import com.miso202402.SportApp.src.models.request.RoutsRequest
import com.miso202402.SportApp.src.models.request.SportSessionPutRequest
import com.miso202402.SportApp.src.models.request.SportSessionRequest
import com.miso202402.SportApp.src.models.request.SportProfileRequest
import com.miso202402.SportApp.src.models.request.TrainingPlanRequest
import com.miso202402.SportApp.src.models.request.TrainingSessionRequest
import com.miso202402.SportApp.src.models.response.DeleteSportSessionResponse
import com.miso202402.SportApp.src.models.response.GetAllEatingRoutineResponse
import com.miso202402.SportApp.src.models.response.GetAllEventsResponse
import com.miso202402.SportApp.src.models.response.GetAllRestRoutineResponse
import com.miso202402.SportApp.src.models.response.GetAllConsultationSessionsResponse
import com.miso202402.SportApp.src.models.response.GetAllDoctorsResponse
import com.miso202402.SportApp.src.models.response.GetAllSportSessionResponse
import com.miso202402.SportApp.src.models.response.GetAllTrainersResponse
import com.miso202402.SportApp.src.models.response.GetAllUserTrainingSessionsResponse
import com.miso202402.SportApp.src.models.response.GetConsultationByIdResponse
import com.miso202402.SportApp.src.models.response.GetDoctor
import com.miso202402.SportApp.src.models.response.InstructionTrainingPlansResponse
import com.miso202402.SportApp.src.models.response.ObjetiveTrainingPlanResponse
import com.miso202402.SportApp.src.models.response.GetEventResponse
import com.miso202402.SportApp.src.models.response.GetFoodRoutineResponse
import com.miso202402.SportApp.src.models.response.GetInfoUserResponse
import com.miso202402.SportApp.src.models.response.GetRestRoutineResponse
import com.miso202402.SportApp.src.models.response.GetRoutsResponse
import com.miso202402.SportApp.src.models.response.GetSportSessionResponse
import com.miso202402.SportApp.src.models.response.RiskTrainingPlanResponse
import com.miso202402.SportApp.src.models.response.GetTrainersByIdResponse
import com.miso202402.SportApp.src.models.response.PutSportObjectiveSessionResponse
import com.miso202402.SportApp.src.models.response.PutSportSessionResponse
import com.miso202402.SportApp.src.models.response.SportProfileResponse
import com.miso202402.SportApp.src.models.response.TraingSessionResponse
import com.miso202402.SportApp.src.models.response.TrainingListPlansResponse
import com.miso202402.SportApp.src.models.response.TrainingPlanResponse
import com.miso202402.SportApp.src.models.response.ValidateTokenResponse
import com.miso202402.front_miso_pf2_g8_sportapp.src.models.request.LoginRequest
import com.miso202402.front_miso_pf2_g8_sportapp.src.models.response.TrainingPlansResponse
import com.miso202402.front_miso_pf2_g8_sportapp.src.models.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
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

    @GET("training_plan/{training_plan_id}")
    fun getTrainingPlan(@Path("training_plan_id") training_plan_id: String): Call<TrainingPlanResponse>

    @GET("eating_routing_training_plan")
    fun getAllEatingRoutines():Call<GetAllEatingRoutineResponse>

    @GET("eating_routing_training_plan/{id_food_routine}")
    fun getFoodRoutine(@Path("id_food_routine") id_food_routine: String):Call<GetFoodRoutineResponse>

    @GET("rest_routine_training_plan")
    fun getAllRestRoutineResponse():Call<GetAllRestRoutineResponse>

    @GET("rest_routine_training_plan/{id_rest_routine}")
    fun getRestRoutine(@Path("id_rest_routine") id_rest_routine: String):Call<GetRestRoutineResponse>

    @POST("objetive_training_plan")
    fun createObjetiveTrainingPlan(@Body request: ObjetiveTrainingPlanRequest): Call<ObjetiveTrainingPlanResponse>

    @POST("instruction_training_plan")
    fun createInstructionTrainingPlan(@Body request: InstructionTrainingPlanRequest?): Call<InstructionTrainingPlansResponse>

    @POST("risk_alerts_training_plan")
    fun createRiskAlertsTrainingPlan(@Body request: RiskAlertsTrainingPlanRequest?): Call<RiskTrainingPlanResponse>

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

    @GET("consultation/consultations")
    fun getAllConsultationsSessions():Call<GetAllConsultationSessionsResponse>

    @GET("consultation/consultation/{id}")
    fun getConsultationBYId(@Path("id") id: String):Call<GetConsultationByIdResponse>

    @GET("consultation/consultations/user/{id}")
    fun getConsultationBYuserId(@Path("id") id: String):Call<GetAllConsultationSessionsResponse>

    @POST("consultation/consultations")
    fun createConsultation (@Body request: ConsultationRequest):Call<GetConsultationByIdResponse>

    @PUT("consultation/consultations/{id}")
    fun updateConsultation (@Path("id") id: String):Call<GetConsultationByIdResponse>

    @GET("sportsSpecialist/doctors")
    fun getAllDoctors():Call<GetAllDoctorsResponse>

    @GET("sportsSpecialist/doctor/{id}")
    fun getDoctorsByID(@Path("id") id: String):Call<GetDoctor>

    @GET("sportsSpecialist/trainers")
    fun getAllTrainers():Call<GetAllTrainersResponse>

    @GET("sportsSpecialist/trainer/{id}")
    fun getTrainerssByID(@Path("id") id: String):Call<GetTrainersByIdResponse>

    @POST("sport_session")
    fun postSportSession(@Body request:SportSessionRequest):Call<GetAllSportSessionResponse>

    @GET("sport_session")
    fun getSportSessions():Call<GetAllSportSessionResponse>

    @GET("sport_user_session/{id}")
    fun getSportUserSessions(@Path("id") id: String):Call<GetAllSportSessionResponse>

    @GET("sport_session/{id}")
    fun getSportSessionById(@Path("id") id: String):Call<GetSportSessionResponse>

    @PUT("sport_session/{id}")
    fun putSportSessionById(@Path("id") id: String, @Body request: SportSessionPutRequest):Call<PutSportSessionResponse>

    @DELETE("sport_session/{id}")
    fun deleteSportSessionById(@Path("id") id: String):Call<DeleteSportSessionResponse>

    @PUT("sport_session_objective/{id}")
    fun putSportSessionById(@Path("id") id: String, @Body request:SportObjectiveSession):Call<PutSportObjectiveSessionResponse>
    @GET("user/{id}")
    fun getInfoUser(@Header("Authorization") authorization: String, @Path("id") id: String):Call<GetInfoUserResponse>

    @POST("sport_profile")
    fun createSportProfile(@Body request: SportProfileRequest):Call<SportProfileResponse>

    @PUT("sport_profile/{id}")
    fun updateSportProfile(@Path("id") id: String, @Body request: SportProfileRequest):Call<SportProfileResponse>

    @GET("sport_profile/{id}")
    fun getOneSportProfileById(@Path("id") id: String):Call<SportProfileResponse>



}
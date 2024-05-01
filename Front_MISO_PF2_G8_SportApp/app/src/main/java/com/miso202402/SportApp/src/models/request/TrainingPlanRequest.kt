package com.miso202402.SportApp.src.models.request

import com.google.gson.annotations.SerializedName

class TrainingPlanRequest (@SerializedName("name") var name: String,
                           @SerializedName("description") var description: String,
                           @SerializedName("weeks") var weeks: Int,
                           @SerializedName("lunes_enabled") var lunes_enabled: Int,
                           @SerializedName("martes_enabled") var martes_enabled: Int,
                           @SerializedName("miercoles_enabled") var miercoles_enabled: Int,
                           @SerializedName("jueves_enabled") var jueves_enabled: Int,
                           @SerializedName("viernes_enabled") var viernes_enabled: Int,
                           @SerializedName("typePlan") var typePlan: String,
                           @SerializedName("sport") var sport: String,
                           @SerializedName("id_rest_routine") var id_rest_routine: String,
                           @SerializedName("id_eating_routine") var id_eating_routine: String
    ) {
}
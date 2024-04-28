package com.miso202402.SportApp.src.models.models

import com.google.gson.annotations.SerializedName


class TrainingPlan(
    @SerializedName("id") var id:String,
    @SerializedName("name") var name:String,
    @SerializedName("description") var description:String,
    @SerializedName("lunes_enabled") var lunes_enabled:Int,
    @SerializedName("martes_enabled") var martes_enabled:Int,
    @SerializedName("miercoles_enabled") var miercoles_enabled:Int,
    @SerializedName("Jueves_enabled") var Jueves_enabled:Int,
    @SerializedName("viernes_enabled") var viernes_enabled:Int,
    @SerializedName("type_plan") var type_plan:String,
    @SerializedName("sport") var sport:String,
    @SerializedName("objectives") var objectives: Array<Objective>? = null) {
}
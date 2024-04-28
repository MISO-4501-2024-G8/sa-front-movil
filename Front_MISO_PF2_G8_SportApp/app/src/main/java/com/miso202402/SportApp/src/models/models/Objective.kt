package com.miso202402.SportApp.src.models.models

import com.google.gson.annotations.SerializedName

class Objective (
    @SerializedName("id") var id:String?,
    @SerializedName("day") var day:String?,
    @SerializedName("repeats") var repeats:Int?,
    @SerializedName("type_objective") var type_objective:String?,
    @SerializedName("id_training_plan") var id_training_plan:Int?,
    @SerializedName("id_rest_routine") var id_rest_routine:Int?,
    @SerializedName("instructions") var instructions: Array<Instruction>? = null){
}
package com.miso202402.SportApp.src.models.models

import com.google.gson.annotations.SerializedName

class Objective (
    @SerializedName("id") var id:String?,
    @SerializedName("id_routine") var id_routine:String?,
    @SerializedName("day") var day:String?,
    @SerializedName("repeats") var repeats:Int?,
    @SerializedName("type_objective") var type_objective:String?,
    @SerializedName("instructions") var instructions:List<Instruction>?,
    @SerializedName("checked") var checked:Boolean?=false,
    ){
}
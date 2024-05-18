package com.miso202402.SportApp.src.models.models

import com.google.gson.annotations.SerializedName

class SportSession (
    @SerializedName("id") var id:String?,
    @SerializedName("name") var name:String?,
    @SerializedName("week") var week:Int?,
    @SerializedName("day") var day:String?,
    @SerializedName("repeats") var repeats:Int?,
    @SerializedName("location") var location:String?,
    @SerializedName("total_time") var total_time:Int?,
    @SerializedName("session_event") var session_event:String?,
    @SerializedName("qty_objectives_achived") var qty_objectives_achived:Int?,
    @SerializedName("objective_instructions") var objective_instructions: List<SportObjectiveSession>? = null,
){
}
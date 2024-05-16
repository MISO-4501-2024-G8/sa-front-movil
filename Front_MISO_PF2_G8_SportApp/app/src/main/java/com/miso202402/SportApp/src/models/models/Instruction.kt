package com.miso202402.SportApp.src.models.models

import com.google.gson.annotations.SerializedName

class Instruction(
    @SerializedName("id") var id:String?,
    @SerializedName("id_objective") var id_objective:String?,
    @SerializedName("instruction_description") var instruction_description:String?,
    @SerializedName("instruction_time") var instruction_time:Int?) {
}
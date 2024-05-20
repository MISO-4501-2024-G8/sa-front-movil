package com.miso202402.SportApp.src.models.models

import com.google.gson.annotations.SerializedName

class SportObjectiveSession(
    @SerializedName("id") var id:String?,
    @SerializedName("instruction_description") var instruction_description:String?,
    @SerializedName("instruction_time") var instruction_time:Int?,
    @SerializedName("target_achieved") var target_achieved:Int?,
) {}
package com.miso202402.SportApp.src.models.models

import com.google.gson.annotations.SerializedName

class RiskAlert(
    @SerializedName("training_plan_id") var training_plan_id:String?,
    @SerializedName("stop_training") var stop_training:Int?,
    @SerializedName("notifications") var notifications:Int?,
    @SerializedName("enable_phone") var enable_phone:Int?,
) {}
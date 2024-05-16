package com.miso202402.SportApp.src.models.request

import com.google.gson.annotations.SerializedName
import com.miso202402.SportApp.src.models.models.Objective
import com.miso202402.SportApp.src.models.models.RiskAlert

class RiskAlertsTrainingPlanRequest(
    @SerializedName("stop_training") var stop_training: String,
    @SerializedName("notifications") var notifications: String,
    @SerializedName("enable_phone") var enable_phone: String,
    @SerializedName("id_training_plan") var id_training_plan: String
) {}
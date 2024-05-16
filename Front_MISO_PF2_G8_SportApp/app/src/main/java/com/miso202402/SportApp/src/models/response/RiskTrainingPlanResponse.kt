package com.miso202402.SportApp.src.models.response

import com.google.gson.annotations.SerializedName
import com.miso202402.SportApp.src.models.models.RiskAlert

class RiskTrainingPlanResponse(
    @SerializedName("message") var message: String?,
    @SerializedName("risk_alerts") var risk_alerts: RiskAlert?,
    @SerializedName("code") var code: Int?,
    @SerializedName("error") var error: String?
)
{}
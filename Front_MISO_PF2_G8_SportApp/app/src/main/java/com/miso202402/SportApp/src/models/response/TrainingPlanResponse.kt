package com.miso202402.SportApp.src.models.response

import com.google.gson.annotations.SerializedName
import com.miso202402.SportApp.src.models.models.TrainingPlan

class TrainingPlanResponse (
    @SerializedName("message") var message: String?,
    @SerializedName("training_plan") var training_plan: TrainingPlan?,
    @SerializedName("code") var code: Int?,
    @SerializedName("error") var error: String?
)
{}
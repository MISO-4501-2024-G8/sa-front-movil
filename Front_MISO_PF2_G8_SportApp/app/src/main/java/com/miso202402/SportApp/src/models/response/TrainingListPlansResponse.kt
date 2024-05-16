package com.miso202402.SportApp.src.models.response

import com.google.gson.annotations.SerializedName
import com.miso202402.SportApp.src.models.models.TrainingPlan

class TrainingListPlansResponse(
    @SerializedName("message") var message: String?,
    @SerializedName("training_plans") var training_plans: List<TrainingPlan>?,
    @SerializedName("code") var code: Int?,
    @SerializedName("error") var error: String?
) {}
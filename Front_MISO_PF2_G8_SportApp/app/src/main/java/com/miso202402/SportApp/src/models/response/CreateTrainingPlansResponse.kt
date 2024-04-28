package com.miso202402.front_miso_pf2_g8_sportapp.src.models.response

import com.google.gson.annotations.SerializedName
import com.miso202402.SportApp.src.models.models.TrainingPlan

data class CreateTrainingPlansResponse(@SerializedName("message") var message: String?,
                                       @SerializedName("training_plan") var trainingPlan: TrainingPlan?,
                                       @SerializedName("code") var code: Int?,
                                       @SerializedName("error") var error: String?){

}
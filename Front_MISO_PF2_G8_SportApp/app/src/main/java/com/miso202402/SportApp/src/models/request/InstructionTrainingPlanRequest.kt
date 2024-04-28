package com.miso202402.SportApp.src.models.request

import com.google.gson.annotations.SerializedName

class InstructionTrainingPlanRequest(@SerializedName("instruction_description") var instruction_description: String,
                                     @SerializedName("instruction_time") var instruction_time: String,
                                     @SerializedName("id_objective") var id_objective: String) {
}
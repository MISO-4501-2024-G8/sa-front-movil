package com.miso202402.SportApp.src.models.request

import com.google.gson.annotations.SerializedName

class ObjetiveTrainingPlanRequest(@SerializedName("day") var day: String,
                                  @SerializedName("objective_repeats") var objective_repeats: String,
                                  @SerializedName("type_objective") var type_objective: String,
                                  @SerializedName("id_training_plan") var id_training_plan: String,
                                  @SerializedName("id_rest_routine") var id_rest_routine: String?
) {
}
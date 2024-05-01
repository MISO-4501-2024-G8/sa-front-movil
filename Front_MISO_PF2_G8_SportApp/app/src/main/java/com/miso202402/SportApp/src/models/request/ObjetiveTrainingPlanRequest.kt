package com.miso202402.SportApp.src.models.request

import com.google.gson.annotations.SerializedName

class ObjetiveTrainingPlanRequest(@SerializedName("day") var day: String?,
                                  @SerializedName("objective_repeats") var objective_repeats: Int?,
                                  @SerializedName("type_objective") var type_objective: String?,
                                  @SerializedName("id_routine") var id_routine: String?) {
}
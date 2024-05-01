package com.miso202402.SportApp.src.models.response

import com.google.gson.annotations.SerializedName
import com.miso202402.SportApp.src.models.models.Objective

class ObjetiveTrainingPlanResponse(@SerializedName("message") var message: String?,
                                   @SerializedName("objective") var objective: Objective?,
                                   @SerializedName("code") var code: Int?,
                                   @SerializedName("error") var error: String?){

}

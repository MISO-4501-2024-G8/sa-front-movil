package com.miso202402.SportApp.src.models.response

import com.google.gson.annotations.SerializedName
import com.miso202402.SportApp.src.models.models.Trainers

class GetTrainersByIdResponse (@SerializedName("message") var message: String?,
                               @SerializedName("content") var content: Trainers?,
                               @SerializedName("code") var code: Int?,
                               @SerializedName("error") var error: String?) {
}
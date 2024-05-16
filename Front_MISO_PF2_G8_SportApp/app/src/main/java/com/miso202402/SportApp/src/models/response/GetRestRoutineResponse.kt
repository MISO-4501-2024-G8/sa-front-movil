package com.miso202402.SportApp.src.models.response

import com.google.gson.annotations.SerializedName
import com.miso202402.SportApp.src.models.models.RestRoutine

class GetRestRoutineResponse(
    @SerializedName("message") var message: String,
    @SerializedName("rest_routine") var rest_routine: RestRoutine,
    @SerializedName("code") var code: Int,
    @SerializedName("error") var error: String
) {}
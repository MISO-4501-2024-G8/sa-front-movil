package com.miso202402.SportApp.src.models.response

import com.google.gson.annotations.SerializedName
import com.miso202402.SportApp.src.models.models.FoodRoutine
import com.miso202402.SportApp.src.models.models.RestRoutine

class GetAllRestRoutineResponse (
    @SerializedName("message") var message: String,
    @SerializedName("rest_routines") var rest_routines: List<RestRoutine>,
    @SerializedName("code") var code: Int,
    @SerializedName("error") var error: String
) {}
package com.miso202402.SportApp.src.models.response

import com.google.gson.annotations.SerializedName
import com.miso202402.SportApp.src.models.models.EatingRoutine

class GetFoodRoutineResponse (
    @SerializedName("message") var message: String,
    @SerializedName("eating_routine") var eating_routine: EatingRoutine,
    @SerializedName("code") var code: Int,
    @SerializedName("error") var error: String
){}
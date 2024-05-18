package com.miso202402.SportApp.src.models.request

import com.google.gson.annotations.SerializedName

class SportObjectiveSessionRequest(
    @SerializedName("target_achieved") var target_achieved: Int?,
) {}
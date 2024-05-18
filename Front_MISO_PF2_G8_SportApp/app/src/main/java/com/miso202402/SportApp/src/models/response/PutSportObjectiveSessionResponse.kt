package com.miso202402.SportApp.src.models.response

import com.google.gson.annotations.SerializedName
import com.miso202402.SportApp.src.models.models.SportObjectiveSession

class PutSportObjectiveSessionResponse (
    @SerializedName("message") var message: String?,
    @SerializedName("content") var content: SportObjectiveSession?,
    @SerializedName("code") var code: Int,
    @SerializedName("error") var error: String?
) {}
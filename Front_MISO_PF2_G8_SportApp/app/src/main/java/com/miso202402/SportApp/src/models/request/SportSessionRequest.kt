package com.miso202402.SportApp.src.models.request

import com.google.gson.annotations.SerializedName

class SportSessionRequest(
    @SerializedName("id_training_session") var id_training_session: String?,
    @SerializedName("week") var week: Int?,
    @SerializedName("day") var day: String?,
    @SerializedName("location") var location: String?,
    @SerializedName("session_event") var session_event: String?
) {}
package com.miso202402.SportApp.src.models.request

import com.google.gson.annotations.SerializedName

class SportSessionPutRequest (
    @SerializedName("total_time") var total_time: Int?,
)
{}
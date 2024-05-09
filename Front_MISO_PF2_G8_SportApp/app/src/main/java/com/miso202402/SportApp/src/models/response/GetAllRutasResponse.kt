package com.miso202402.SportApp.src.models.response

import com.google.gson.annotations.SerializedName
import com.miso202402.SportApp.src.models.models.Events
import com.miso202402.SportApp.src.models.models.Routs

class GetAllRutasResponse ( @SerializedName("message") var message: String,
                            @SerializedName("content") var content: List<Routs>,
                            @SerializedName("code") var code: Int,
                            @SerializedName("error") var error: String) {
}
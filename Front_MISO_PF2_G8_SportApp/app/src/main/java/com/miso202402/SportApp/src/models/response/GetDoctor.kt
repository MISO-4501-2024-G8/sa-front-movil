package com.miso202402.SportApp.src.models.response

import com.google.gson.annotations.SerializedName
import com.miso202402.SportApp.src.models.models.Doctors

class GetDoctor (@SerializedName("message") var message: String?,
                 @SerializedName("doctor") var content: Doctors?,
                 @SerializedName("code") var code: Int?,
                 @SerializedName("error") var error: String?) {
}
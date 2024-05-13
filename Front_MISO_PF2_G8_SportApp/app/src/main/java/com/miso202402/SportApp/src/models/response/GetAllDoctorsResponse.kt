package com.miso202402.SportApp.src.models.response

import com.google.gson.annotations.SerializedName
import com.miso202402.SportApp.src.models.models.Doctors

class GetAllDoctorsResponse (@SerializedName("message") var message: String?,
                             @SerializedName("doctors") var content: List<Doctors>?,
                             @SerializedName("code") var code: Int?,
                             @SerializedName("error") var error: String?){
}
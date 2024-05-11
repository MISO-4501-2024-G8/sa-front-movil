package com.miso202402.SportApp.src.models.response

import com.google.gson.annotations.SerializedName
import com.miso202402.SportApp.src.models.models.Events

class GetEventResponse (@SerializedName("message") var message: String?,
                        @SerializedName("content") var content: Events?,
                        @SerializedName("code") var code: Int?,
                        @SerializedName("error") var error: String?){
}
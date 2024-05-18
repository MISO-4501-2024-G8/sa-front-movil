package com.miso202402.SportApp.src.models.response

import com.google.gson.annotations.SerializedName
import com.miso202402.SportApp.src.models.models.SportProfile

class SportProfileResponse (@SerializedName("message") var message: String?,
                            @SerializedName("user_profile") var user_profile: SportProfile?,
                            @SerializedName("code") var code: Int?,
                            @SerializedName("error") var error: String?) {
}
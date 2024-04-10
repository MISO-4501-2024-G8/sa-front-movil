package com.miso202402.front_miso_pf2_g8_sportapp.src.models.response

import com.google.gson.annotations.SerializedName

data class LoginResponse (@SerializedName("message") var message: String,
                          @SerializedName("token") var token: String,
                          @SerializedName("id") var id: String,
                          @SerializedName("expirationToken") var expirationToken: String,
                          @SerializedName("error") var error: String){
}
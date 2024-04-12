package com.miso202402.front_miso_pf2_g8_sportapp.src.models.request

import com.google.gson.annotations.SerializedName

data class LoginRequest (@SerializedName("email") var email: String,
                         @SerializedName("password") var password: String){
}
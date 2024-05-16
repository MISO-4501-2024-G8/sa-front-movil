package com.miso202402.SportApp.src.models.response

import com.google.gson.annotations.SerializedName

class ValidateTokenResponse(
    @SerializedName("message") var message: String,
    @SerializedName("code") var code: Int,
    @SerializedName("exp") var exp: Long,
    @SerializedName("expirationDate") var expirationDate: String,
    @SerializedName("userType") var userType: Int,
    @SerializedName("typePlan") var typePlan: String,
    @SerializedName("error") var error: String
)
{}
package com.miso202402.SportApp.src.models.response

import com.google.gson.annotations.SerializedName
import com.miso202402.SportApp.src.models.models.UserDetail

class GetInfoUserResponse (@SerializedName("message") var message: String?,
                           @SerializedName("id") var id: String?,
                           @SerializedName("email") var email: String?,
                           @SerializedName("password") var password: String?,
                           @SerializedName("doc_num") var doc_num: String?,
                           @SerializedName("doc_type") var doc_type: String?,
                           @SerializedName("name") var name: String?,
                           @SerializedName("phone") var phone: String?,
                           @SerializedName("user_type") var user_type: String,
                           @SerializedName("token") var token: String?,
                           @SerializedName("expirationToken") var expirationToken: String?,
                           @SerializedName("detail") var detail: UserDetail?,
                           @SerializedName("code") var code: Int?,
                           @SerializedName("error") var error: String?){
}
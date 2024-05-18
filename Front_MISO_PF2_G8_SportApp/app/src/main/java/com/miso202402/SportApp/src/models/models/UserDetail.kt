package com.miso202402.SportApp.src.models.models

import com.google.gson.annotations.SerializedName

class UserDetail(@SerializedName("id") var id: String?,
                 @SerializedName("gender") var gender: String?,
                 @SerializedName("age") var age: Float?,
                 @SerializedName("weight") var weight: Float?,
                 @SerializedName("height") var height: Float?,
                 @SerializedName("birth_country") var birth_country: String?,
                 @SerializedName("birth_city") var birth_city: String?,
                 @SerializedName("residence_country") var residence_country: String,
                 @SerializedName("residence_city") var token: String?,
                 @SerializedName("residence_seniority") var residence_seniority: Int?,
                 @SerializedName("sports") var sports: String?,
                 @SerializedName("typePlan") var typePlan: String?,
                 @SerializedName("acceptance_notify") var acceptance_notify: Int?,
                 @SerializedName("acceptance_tyc") var acceptance_tyc: Int?,
                 @SerializedName("acceptance_personal_data") var acceptance_personal_data: Int?) {
}
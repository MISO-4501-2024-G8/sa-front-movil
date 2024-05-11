package com.miso202402.SportApp.src.models.models
import com.google.gson.annotations.SerializedName
class RestDevice (
    @SerializedName("id") var id:String?,
    @SerializedName("id_rest_routine") var id_rest_routine:String?,
    @SerializedName("name") var name:String?,
    @SerializedName("qty") var qty: Int?,
    @SerializedName("rental_value") var rental_value: Float?,
) {}
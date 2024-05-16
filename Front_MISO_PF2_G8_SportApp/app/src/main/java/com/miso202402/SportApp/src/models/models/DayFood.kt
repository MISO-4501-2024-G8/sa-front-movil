package com.miso202402.SportApp.src.models.models
import com.google.gson.annotations.SerializedName
class DayFood (
    @SerializedName("id") var id:String?,
    @SerializedName("id_eating_routine") var id_rest_routine:String?,
    @SerializedName("day") var day:String?,
    @SerializedName("food") var food:String?,
    @SerializedName("qty") var qty: Int?,
    @SerializedName("calories") var calories: Int?,
    @SerializedName("value") var value: Float?,
    @SerializedName("rental_value") var rental_value: Float?,
) {}
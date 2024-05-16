package com.miso202402.SportApp.src.models.models

import com.google.gson.annotations.SerializedName

class FoodRoutine (
    @SerializedName("id") var id:String?,
    @SerializedName("name") var name:String?,
    @SerializedName("description") var description:String?,
    @SerializedName("weeks") var weeks:Int?,
    @SerializedName("max_weight") var max_weight:Float?,
    @SerializedName("min_weight") var min_weight:Float?,
    @SerializedName("day_food_plans") var day_food_plans: Array<Food>? = null,
) {}
package com.miso202402.SportApp.src.models.models

import com.google.gson.annotations.SerializedName

class FoodObjective(
    @SerializedName("day") var day:String?,
    @SerializedName("foods") var foods: List<DayFood>?
) {}
package com.miso202402.SportApp.src.models.models

import com.google.gson.annotations.SerializedName
class RestRoutine(
    @SerializedName("id") var id:String?,
    @SerializedName("name") var name:String?,
    @SerializedName("description") var description:String?,
    @SerializedName("objectives") var objectives: Array<Objective>? = null,
    @SerializedName("rest_devices") var rest_devices: Array<RestDevice>? = null
) {}
package com.miso202402.SportApp.src.models.models

import com.google.gson.annotations.SerializedName

class Doctors (@SerializedName("id") var id:String?,
               @SerializedName("id_third_product") var id_third_product :String?,
               @SerializedName("address") var address  :String?,
               @SerializedName("phone") var phone:String?) {
}
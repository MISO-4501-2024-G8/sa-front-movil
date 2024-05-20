package com.miso202402.SportApp.src.models.models

import com.google.gson.annotations.SerializedName

class SportProfileModel  (@SerializedName("process_type") var process_type: String?,
                          @SerializedName("state") var state: Boolean?,
                          @SerializedName("contenedor") var contenedor: String?) {
}
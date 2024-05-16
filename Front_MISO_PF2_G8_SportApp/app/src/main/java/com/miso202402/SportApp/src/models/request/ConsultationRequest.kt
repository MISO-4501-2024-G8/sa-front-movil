package com.miso202402.SportApp.src.models.request

import com.google.gson.annotations.SerializedName

class ConsultationRequest  (@SerializedName("id_service_worker") var id_service_worker: String?,
                            @SerializedName("id_user") var id_user: String?,
                            @SerializedName("consultation_type") var consultation_type: String?,
                            @SerializedName("consultation_date") var consultation_date: String?,
                            @SerializedName("link") var session_date: String?){
}
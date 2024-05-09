package com.miso202402.SportApp.src.models.models;

import com.google.gson.annotations.SerializedName;

class TrainingSession (
    @SerializedName("id") var id:String,
    @SerializedName("id_sport_user") var id_sport_user:String?,
    @SerializedName("id_event") var id_event:String?,
    @SerializedName("event_category") var event_category:String?,
    @SerializedName("sport_type") var sport_type:String?,
    @SerializedName("session_date") var session_date:String?
) { }

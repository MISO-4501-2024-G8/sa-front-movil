package com.miso202402.SportApp.src.models.request

import com.google.gson.annotations.SerializedName

class EventsRequest(@SerializedName("event_name") var event_name: String?,
                    @SerializedName("event_description") var event_description: String?,
                    @SerializedName("event_location") var event_location: String?,
                    @SerializedName("event_type") var event_type: String?,
                    @SerializedName("sport") var sport: String?,
                    @SerializedName("link") var link: String?,
                    @SerializedName("event_date") var event_date: String?) {
}
package com.miso202402.SportApp.src.models.request

import com.google.gson.annotations.SerializedName

class RoutsRequest (@SerializedName("route_name") var route_name: String,
                    @SerializedName("route_description") var route_description: String,
                    @SerializedName("route_location_A") var route_location_A: String,
                    @SerializedName("route_location_B") var route_location_B: String,
                    @SerializedName("route_latlon_A") var route_latlon_A: String,
                    @SerializedName("route_latlon_B") var route_latlon_B: String,
                    @SerializedName("route_type") var event_date: String,
                    @SerializedName("route_date") var route_date: String,
                    @SerializedName("sport") var sport: String,
                    @SerializedName("link") var link: String) {
}
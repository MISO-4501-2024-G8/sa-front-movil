package com.miso202402.SportApp.src.models.models

import com.google.gson.annotations.SerializedName

class SportProfile (@SerializedName("user_id") var user_id: String?,
                    @SerializedName("sh_caminar") var sh_caminar: Int?,
                    @SerializedName("sh_trotar") var sh_trotar: Int?,
                    @SerializedName("sh_correr") var sh_correr: Int?,
                    @SerializedName("sh_nadar") var sh_nadar: Int?,
                    @SerializedName("sh_bicicleta") var sh_bicicleta: Int?,
                    @SerializedName("pp_fractura") var pp_fractura: Int?,
                    @SerializedName("pp_esguinse") var pp_esguinse: Int?,
                    @SerializedName("pp_lumbalgia") var pp_lumbalgia: Int?,
                    @SerializedName("pp_articulacion") var pp_articulacion: Int?,
                    @SerializedName("pp_migranias") var pp_migranias: Int?,
                    @SerializedName("i_vo2max") var i_vo2max: Float?,
                    @SerializedName("i_ftp") var i_ftp: Float?,
                    @SerializedName("i_total_practice_time") var i_total_practice_time: Int?,
                    @SerializedName("i_total_objective_achived") var i_total_objective_achived: Int?,
                    @SerializedName("h_total_calories") var h_total_calories: Float?,
                    @SerializedName("h_avg_bpm") var h_avg_bpm: String?) {
}
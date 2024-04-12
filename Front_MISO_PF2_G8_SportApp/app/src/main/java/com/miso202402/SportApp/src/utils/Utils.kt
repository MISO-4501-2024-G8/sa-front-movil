package com.miso202402.front_miso_pf2_g8_sportapp.src.utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Utils {

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://g7o4mxf762.execute-api.us-east-1.amazonaws.com/prod/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
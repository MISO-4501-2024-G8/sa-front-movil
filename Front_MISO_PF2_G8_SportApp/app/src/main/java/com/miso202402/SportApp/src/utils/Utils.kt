package com.miso202402.front_miso_pf2_g8_sportapp.src.utils

import android.content.Context
import android.widget.Toast
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Utils {

    val DOMAIN_login: String = "https://g7o4mxf762.execute-api.us-east-1.amazonaws.com/prod/"
    val DOMAIN_plan: String = "http://lb-ms-py-training-mngr-157212315.us-east-1.elb.amazonaws.com/"
    fun getRetrofit(domain:String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(domain)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun showMessageDialog(activity: Context?, message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }
}
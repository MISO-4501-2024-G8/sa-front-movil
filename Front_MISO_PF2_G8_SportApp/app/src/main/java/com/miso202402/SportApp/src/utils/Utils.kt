package com.miso202402.front_miso_pf2_g8_sportapp.src.utils

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Utils {

    val DOMAIN: String = "https://g7o4mxf762.execute-api.us-east-1.amazonaws.com/prod/"
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun showMessageDialog(activity: FragmentActivity, message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }
}
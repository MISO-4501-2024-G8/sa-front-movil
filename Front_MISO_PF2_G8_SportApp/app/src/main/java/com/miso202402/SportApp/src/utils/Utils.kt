package com.miso202402.front_miso_pf2_g8_sportapp.src.utils

import LoggingInterceptor
import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import okhttp3.OkHttpClient
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

    fun getRetrofit2(domain:String): Retrofit {

        val loggingInterceptor = LoggingInterceptor()

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(domain)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun showMessageDialog(activity: FragmentActivity, message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }
}
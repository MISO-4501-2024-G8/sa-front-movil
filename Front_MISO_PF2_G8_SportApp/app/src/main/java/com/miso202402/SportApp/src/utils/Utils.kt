package com.miso202402.front_miso_pf2_g8_sportapp.src.utils

import android.content.Context
import android.widget.Toast
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request

class Utils {

    fun getRetrofit(domain:String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(domain)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    class AuthInterceptor(private val token: String) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val originalRequest: Request = chain.request()
            val builder: Request.Builder = originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
            val newRequest: Request = builder.build()
            return chain.proceed(newRequest)
        }
    }

    fun getRetrofitBearer(domain: String, token: String): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(token))
            .build()

        return Retrofit.Builder()
            .baseUrl(domain)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    fun showMessageDialog(activity: Context?, message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
}
package com.miso202402.SportApp.src.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
class SharedPreferences(context: Context) {

    val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREFS_FILENAME, Context.MODE_PRIVATE
    )

    val gson = Gson()

    fun <T> saveData(key: String, data: T) {
        val json = gson.toJson(data)
        sharedPreferences.edit().putString(key, json).apply()
    }

    inline fun <reified T> getData(key: String): T? {
        val json = sharedPreferences.getString(key, null)
        return gson.fromJson(json, object : TypeToken<T>() {}.type)
    }

    fun clearData(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    companion object {
        private const val PREFS_FILENAME = "sport_app_g8_prefs"
    }
}
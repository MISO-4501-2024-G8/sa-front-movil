package com.miso202402.SportApp.src.utils

import android.content.Context
import com.google.gson.Gson
import com.miso202402.SportApp.src.models.models.TrainingPlan

object PreferenceHelper {
    private const val PREF_NAME = "TrainingPlanPreferences"
    private const val KEY_TRAINING_PLAN = "training_plan"

    fun saveTrainingPlan(context: Context, trainingPlan: TrainingPlan) {
        val gson = Gson()
        val json = gson.toJson(trainingPlan)
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_TRAINING_PLAN, json).apply()
    }

    fun getTrainingPlan(context: Context): TrainingPlan? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_TRAINING_PLAN, null)
        return if (json != null) {
            val gson = Gson()
            gson.fromJson(json, TrainingPlan::class.java)
        } else {
            null
        }
    }

    fun clearTrainingPlan(context: Context){
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().clear()
    }
}

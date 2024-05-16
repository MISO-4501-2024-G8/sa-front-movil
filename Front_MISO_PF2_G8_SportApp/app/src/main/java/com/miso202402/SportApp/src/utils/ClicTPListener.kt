package com.miso202402.SportApp.src.utils

import android.view.View
import com.miso202402.SportApp.src.models.models.TrainingPlan


interface ClicTPListener {
    fun onCListItemClick(view: View, trainingPlan: TrainingPlan)
}
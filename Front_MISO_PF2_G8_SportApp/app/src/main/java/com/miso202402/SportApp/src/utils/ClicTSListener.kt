package com.miso202402.SportApp.src.utils

import android.view.View
import com.miso202402.SportApp.src.models.models.TrainingSession

interface ClicTSListener {
    fun onCListTSItemClic(view: View, trainingSession: TrainingSession)
}
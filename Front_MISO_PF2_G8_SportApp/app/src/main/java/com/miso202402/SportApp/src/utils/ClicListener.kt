package com.miso202402.SportApp.src.utils

import android.view.View
import com.miso202402.SportApp.src.models.models.Events

interface ClicListener {
    fun onCListItemClick(view: View, event: Events)
}
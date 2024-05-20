package com.miso202402.SportApp.src.utils

import android.view.View
import com.miso202402.SportApp.src.models.models.SportSession

interface ClickListener_SportSession {
    fun onCListItemClick(view: View, sportSession: SportSession)
}
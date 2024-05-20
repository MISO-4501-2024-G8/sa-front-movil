package com.miso202402.SportApp.src.utils

import android.view.View
import com.miso202402.SportApp.src.models.models.SportProfileModel

interface ClicSportProfile {
    fun onCListSPClic(view: View, list: List<SportProfileModel>)
}
package com.miso202402.SportApp.src.utils

import android.view.View
import com.miso202402.SportApp.src.models.models.RestRoutine

interface ClickListener_restroutines {
    fun onListItemClick(view: View, restRoutine: RestRoutine, position:Int)
    fun onListItemLongClick(view: View, restRoutine: RestRoutine, position:Int)
}
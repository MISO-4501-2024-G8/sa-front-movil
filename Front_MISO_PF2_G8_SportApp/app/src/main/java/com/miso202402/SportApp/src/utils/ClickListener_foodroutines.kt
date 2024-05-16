package com.miso202402.SportApp.src.utils

import android.view.View
import com.miso202402.SportApp.src.models.models.FoodRoutine

interface ClickListener_foodroutines {

    fun onListItemClick(view: View, foodRoutine: FoodRoutine, position:Int)
    fun onListItemLongClick(view: View, foodRoutine: FoodRoutine, position:Int)
}
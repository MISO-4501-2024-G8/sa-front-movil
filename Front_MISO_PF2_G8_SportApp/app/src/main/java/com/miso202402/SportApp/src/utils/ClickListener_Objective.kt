package com.miso202402.SportApp.src.utils

import android.view.View
import com.miso202402.SportApp.src.models.models.Instruction
import com.miso202402.SportApp.src.models.models.Objective

interface ClickListener_Objective {
    fun onAddItemClick(view: View, objective: Objective)
    fun onRemoveItemClick(view: View, instruction: Instruction)
}
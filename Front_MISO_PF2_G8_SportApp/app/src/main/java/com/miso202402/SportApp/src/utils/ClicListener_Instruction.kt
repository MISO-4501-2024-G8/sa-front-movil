package com.miso202402.SportApp.src.utils

import android.view.View
import com.miso202402.SportApp.src.models.models.Instruction

interface ClicListener_Instruction {
    fun onCListItemClick(view: View, instruction: Instruction)
    fun onRemoveItemClick(view: View, instruction: Instruction)
}
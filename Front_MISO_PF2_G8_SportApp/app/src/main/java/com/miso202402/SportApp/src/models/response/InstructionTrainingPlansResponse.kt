package com.miso202402.SportApp.src.models.response

import com.google.gson.annotations.SerializedName
import com.miso202402.SportApp.src.models.models.Instruction
import com.miso202402.SportApp.src.models.models.Objective

class InstructionTrainingPlansResponse (@SerializedName("message") var message: String?,
                                        @SerializedName("instruction") var instruction: Instruction?,
                                        @SerializedName("code") var code: Int?,
                                        @SerializedName("error") var error: String?){

}
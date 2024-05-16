package com.miso202402.SportApp.src.utils

import android.view.View
import com.miso202402.SportApp.src.models.models.ConsultationsSessions
import com.miso202402.SportApp.src.models.models.Routs

interface ClicListener_ProgramConsultation {

    fun onCListItemClick(view: View, consultation: ConsultationsSessions )
}
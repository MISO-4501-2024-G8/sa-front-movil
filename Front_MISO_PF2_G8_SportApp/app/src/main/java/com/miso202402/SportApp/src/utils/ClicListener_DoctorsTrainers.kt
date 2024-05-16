package com.miso202402.SportApp.src.utils

import android.view.View
import com.miso202402.SportApp.src.models.models.Doctors
import com.miso202402.SportApp.src.models.models.Trainers

interface ClicListener_DoctorsTrainers {

    fun onCListItemClick(view: View, doctor: Doctors?, trainer: Trainers?)
}
package com.miso202402.SportApp.src.utils

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.miso202402.SportApp.src.models.models.Events
import com.miso202402.SportApp.src.models.models.Routs
import com.miso202402.SportApp.src.models.models.SportProfileModel
import com.miso202402.front_miso_pf2_g8_sportapp.R

class
AddSportAdapterAdapter (
    private val listProfile:  List<SportProfileModel>,val clickListener: ClicSportProfile) : RecyclerView.Adapter<AddSportAdapterAdapter.ViewHolder>(){

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvName: CheckBox
        var textTipoEvento: TextView

        init {
            tvName = view.findViewById(R.id.checkBoxSport_Preferences)
            textTipoEvento = view.findViewById(R.id.textTipoEvento)

        }
    }

    @SuppressLint("ResourceType")
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AddSportAdapterAdapter.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.check_sport_profile, viewGroup, false)

        return AddSportAdapterAdapter.ViewHolder(view)
    }

    fun List<Events>.findFirstEventById(id: String): Events? {
        return this.firstOrNull { it.id == id }
    }
    fun List<Routs>.findFirstRouteById(id: String): Routs? {
        return this.firstOrNull { it.id == id }
    }
    override fun onBindViewHolder(viewHolder: AddSportAdapterAdapter.ViewHolder, position: Int) {
        val stateProfile = listProfile[position]
        val checkboNamex = listProfile[position].process_type
        val contenedor = listProfile[position].contenedor

        viewHolder.tvName.setText(checkboNamex)
        viewHolder.textTipoEvento.setText(contenedor)
        viewHolder.tvName.isChecked = (stateProfile.state?:false)

        viewHolder.tvName.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                Log.i("entre" +checkboNamex, "checkboxEnabled  $isChecked")
                listProfile[position].state = true
            }
            else{
                listProfile[position].state = false
            }
        }
        viewHolder.itemView.setOnClickListener {
            clickListener.onCListSPClic(it, listProfile)
        }

    }

    override fun getItemCount(): Int {
        return listProfile.size
    }
}
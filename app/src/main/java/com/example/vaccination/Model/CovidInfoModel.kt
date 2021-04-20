package com.example.vaccination.Model

import java.text.SimpleDateFormat
import java.util.*

data class CovidInfoModel(
    val region: String,
    val newPositive: Number,
    val deaths: Number,
    val swap: Number,
    val posPercentage: Number,
    val icu: Number,
    val incidence: Number,
    val totVaccine: Number,
    val dateInfo: Date? = null
) {
    override fun toString() : String {
        if (dateInfo == null) {
            return "No data available"
        } else {
            val formatter = SimpleDateFormat("HH:mm:ss dd/MM/yyyy")
            val formattedDate = formatter.format(dateInfo)
            return "Covid situation in $region updated as of $formattedDate:\n" +
                    " • New covid positive: $newPositive \n" +
                    " • Today's deaths: $deaths \n" +
                    " • New swaps: $swap \n" +
                    " • Percentage of positives over the swaps: $posPercentage \n" +
                    " • New icu: $icu \n" +
                    " • Total number of vaccines administered: $totVaccine"
        }
    }
}

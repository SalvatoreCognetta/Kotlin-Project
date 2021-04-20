package com.example.vaccination.Model

import com.google.gson.annotations.SerializedName
import java.util.*

data class VaccinePrenotationModel(
        @SerializedName("uid") val uid: String,
        @SerializedName("covidCenter") val covidCenter: String,
        @SerializedName("dateRequest") val dateRequest: Date? = null,
        @SerializedName("dateFirstDose") val dateFirstDose: Date? = null,
        @SerializedName("dateSecondDose") val dateSecondDose: Date? = null
)

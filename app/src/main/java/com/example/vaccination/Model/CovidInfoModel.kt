package com.example.vaccination.Model

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
    val dateInfo: Date
)

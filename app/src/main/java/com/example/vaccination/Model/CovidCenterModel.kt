package com.example.vaccination.Model

import android.location.Location

data class CovidCenterModel(
    val _id: String,
    val location: String,
    val region: String,
    val latitude: Double,
    val longitude: Double
) {
    fun getLocation() : Location {
        var loc = Location("")
        loc.longitude = longitude
        loc.latitude = latitude

        return loc
    }
}

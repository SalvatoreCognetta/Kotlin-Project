package com.example.vaccination.Utils

import android.location.Location
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * Validate the email, checking if it's inserted the correct pattern.
 *
 */
fun validateEmail(emailText: String) : String {
    // Check if the email input is filled and correct
    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    if (emailText.isEmpty()) {
        return "Enter email address"
    } else if (!emailText.trim { it <= ' ' }.matches(emailPattern.toRegex())) {
        return "Invalid email address"
    }
    return ""
}

/**
 * Go from the [fragment] back to the previous activity
 */
fun fromFragmentToActivity(supportFragmentManager: FragmentManager, fragment: Fragment) {
    supportFragmentManager?.beginTransaction()?.remove(fragment)?.commit()
}

/**
 * Get closest location between [startLocation] and a list of [locations]
 */
fun getClosestLocation(startLocation: Location, locations: List<Location>) : Pair<Location, Int> {
    var closestLocation: Location = locations[0]
    var smallestDistance = -1f
    var index = 0

    for (i in 0 until locations.size) {
        val distanceInMeters = startLocation.distanceTo(locations[i])
        if (smallestDistance == -1f || distanceInMeters < smallestDistance) {
            smallestDistance = distanceInMeters
            closestLocation = locations[i]
            index = i
        }
    }

    return Pair(closestLocation, index)
}
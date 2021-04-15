package com.example.vaccination.Utils

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
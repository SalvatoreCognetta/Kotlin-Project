package com.example.vaccination.Firebase

import com.google.firebase.database.Exclude


data class User(
    var name: String? = null,
    var surname: String? = null,
    var email: String? = null,
    var birthday: String? = null,
    var region: String? = null,
    var uid: String? = null) {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "surname" to surname,
            "email" to email,
            "birthday" to birthday,
            "region" to region,
            "uid" to uid
        )
    }

    companion object {
        const val user_table_name = "users"
    }
}

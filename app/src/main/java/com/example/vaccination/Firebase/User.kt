package com.example.vaccination.Firebase

import com.google.firebase.database.Exclude


data class User(
    val name: String? = null,
    val surname: String? = null,
    val email: String? = null,
    val birthday: String? = null,
    val region: String? = null,
    val uid: String? = null) {

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

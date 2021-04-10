package com.example.vaccination.Room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="user")
data class User(
        val email: String,
        val pwd: String,
        val salt: String,

        @PrimaryKey(autoGenerate = true)
        val id: Long? = 0L
)

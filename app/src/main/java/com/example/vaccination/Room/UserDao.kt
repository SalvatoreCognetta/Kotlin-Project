package com.example.vaccination.Room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {

    @Insert
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)

    @Query("select * from user order by id")
    fun getAllUsers(): LiveData<List<User>>

    @Query("select COUNT(*) from user where email = :email and pwd = :pwd")
    fun login(email: String, pwd: String): Int // Return 1 if the login is successful
}
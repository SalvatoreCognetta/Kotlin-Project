package com.example.vaccination.Room

class UserRepository(private val userDao: UserDao) {

    val users = userDao.getAllUsers()

    fun insert(user: User) {
        return userDao.insert(user)
    }

    fun isValidAccount(email: String, pwd: String): Boolean {
        return userDao.login(email, pwd) == 1
    }

//    suspend fun getUserName(userName: String):User?{
//        return dao.getUsername(userName)
//    }
}
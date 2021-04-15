package com.example.vaccination.Firebase

import android.util.Log
import com.example.vaccination.Utils.md5
import com.google.firebase.database.DatabaseReference

class UserDao {
    companion object {
        fun insert(db: DatabaseReference, user: User) {
            if (user.email != null) {
                val key = user.uid!! // md5(user.email!!)
                db.child(User.user_table_name).child(key).setValue(user)
            }
        }

        fun get(db: DatabaseReference, uid: String) : User {
            val key = uid!! // md5(email)
            var userRet: User? = User()
            db.child(User.user_table_name).child(key).get().addOnSuccessListener {
                Log.i(TAG, "Got user ${key}")
                userRet = it.getValue(User::class.java)
            }
            return userRet!!
        }

        fun update(db: DatabaseReference, user: User) {
            if (user.email != null) {
                val key = user.uid!! // md5(user.email!!)
                val userValues = user.toMap()
                val childUpdates = hashMapOf<String, Any>(User.user_table_name + "/$key" to userValues)
                db.updateChildren(childUpdates)
            }
        }

        fun delete(db: DatabaseReference, user: User) {
            if (user.email != null) {
                val key = user.uid!! // md5(user.email!!)
                db.child(User.user_table_name).child(key).removeValue()
            }

        }

        private val TAG = "UserDao"
    }
}
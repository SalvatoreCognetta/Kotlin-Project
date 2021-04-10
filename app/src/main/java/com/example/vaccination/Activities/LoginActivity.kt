package com.example.vaccination.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.vaccination.R
import com.example.vaccination.Room.UserRepository
import com.example.vaccination.Room.VaccinationDatabase

class LoginActivity : AppCompatActivity() {

    private var userViewModel: LoginViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginBtnId = findViewById<Button>(R.id.btnLogin)
        val emailId = findViewById<EditText>(R.id.inputEmail)
        val pwdId = findViewById<EditText>(R.id.inputPassword)

//        userViewModel = ViewModelProvider.of(this, LoginViewModelFactory())

        // Login
        loginBtnId?.setOnClickListener() {
            login(emailId, pwdId)
        }

    }

    private fun login(emailId: EditText, pwdId: EditText) {
        val emailText   = emailId.text.toString()
        val pwdText     = pwdId.text.toString()

        if (!validateLogin(emailText, pwdText)) {
            return
        }


        /* else if () {

        }*/
    }

    private fun validateLogin(emailText: String, pwdText: String): Boolean {
        // Check if the email input is filled and correct
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if (emailText.isEmpty()) {
            Toast.makeText(applicationContext, "Enter email address", Toast.LENGTH_SHORT).show()
            return false
        } else if (!emailText.trim { it <= ' ' }.matches(emailPattern.toRegex())) {
            Toast.makeText(applicationContext, "Invalid email address", Toast.LENGTH_SHORT).show()
            return false
        } else if (pwdText.isEmpty()) { // Check if the password input is filled
            Toast.makeText(applicationContext, "Enter password", Toast.LENGTH_SHORT).show()
            return false
        }

        // TODO: Implement your own authentication logic here.

        return true
    }
}
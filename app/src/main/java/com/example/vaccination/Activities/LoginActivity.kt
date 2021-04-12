package com.example.vaccination.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.vaccination.R

class LoginActivity : AppCompatActivity() {

    private var userViewModel: LoginViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val email = findViewById<EditText>(R.id.inputEmail)
        val pwd = findViewById<EditText>(R.id.inputPwd)

        val loginBtn = findViewById<Button>(R.id.btnLogin)
        val signupBtn = findViewById<Button>(R.id.btnSignup)

//        userViewModel = ViewModelProvider.of(this, LoginViewModelFactory())

        // Login
        loginBtn?.setOnClickListener() {
            login(loginBtn, email, pwd)
        }

        // Go to SignUp page
        signupBtn?.setOnClickListener() {
            // Start the Signup activity
            val intent = Intent(applicationContext, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun login(loginBtn: Button, email: EditText, pwd: EditText) {
        val emailText   = email.text.toString()
        val pwdText     = pwd.text.toString()

        if (!validateLoginInput(emailText, pwdText)) {
            return
        }

        loginBtn.isEnabled = false

        // TODO: Implement your own authentication logic here.

    }

    fun onLoginSuccess(loginBtn: Button) {
        loginBtn.isEnabled = true
        // Go to the home
        //startActivity(Intent(this, ...))
    }

    private fun validateLoginInput(emailText: String, pwdText: String): Boolean {
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

        return true
    }
}
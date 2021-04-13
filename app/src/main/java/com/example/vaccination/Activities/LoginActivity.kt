package com.example.vaccination.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.vaccination.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

//    private var userViewModel: LoginViewModel? = null

    private lateinit var email: EditText
    private lateinit var pwd: EditText
    private lateinit var loginBtn: Button
    private lateinit var signupBtn: Button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email = findViewById<EditText>(R.id.inputEmail)
        pwd = findViewById<EditText>(R.id.inputPwd)
        loginBtn = findViewById<Button>(R.id.btnLogin)
        signupBtn = findViewById<Button>(R.id.btnSignup)

        // Initialize Firebase Auth
        auth = Firebase.auth

//        userViewModel = ViewModelProvider.of(this, LoginViewModelFactory())

        // Login
        loginBtn?.setOnClickListener() {
            login()
        }

        // Go to SignUp page
        signupBtn?.setOnClickListener() {
            // Start the Signup activity
            val intent = Intent(applicationContext, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            reload(); // TODO implement
        }
    }

    private fun reload() {
        //TODO("Not yet implemented")
    }

    private fun login() {
        val emailText   = email.text.toString()
        val pwdText     = pwd.text.toString()

        if (!validateLoginInput(emailText, pwdText)) {
            return
        }

        loginBtn.isEnabled = false

        // TODO: Implement your own authentication logic here.
        auth.signInWithEmailAndPassword(emailText, pwdText)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail: success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
                loginBtn.isEnabled = true
            }

    }

    private fun updateUI(user: FirebaseUser?) {
        // TODO implement
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

    companion object {
        private const val TAG = "LoginActivity"
    }
}
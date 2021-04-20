package com.example.vaccination.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.vaccination.Fragments.RecoverPwdFragment
import com.example.vaccination.R
import com.example.vaccination.Utils.validateEmail
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {


    private lateinit var email: EditText
    private lateinit var pwd: EditText
    private lateinit var forgotPwd: TextView
    private lateinit var loginBtn: Button
    private lateinit var signupBtn: Button
    private lateinit var googleSignInBtn: SignInButton

    private lateinit var auth: FirebaseAuth

    private lateinit var googleSignInClient: GoogleSignInClient
    // 720714238520-f99fu5l55dcuop5a92r023431gnhcrv7.apps.googleusercontent.com

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email = findViewById<EditText>(R.id.inputEmail)
        pwd = findViewById<EditText>(R.id.inputPwd)
        forgotPwd = findViewById<TextView>(R.id.forgotPwd)
        loginBtn = findViewById<Button>(R.id.btnLogin)
        signupBtn = findViewById<Button>(R.id.btnSignup)
        googleSignInBtn = findViewById<SignInButton>(R.id.googleSignInBtn)

        // Initialize Firebase Auth
        auth = Firebase.auth

        // Open forgot pwd fragment
        forgotPwd?.setOnClickListener() {
            // create fragment instance
            val fragment : RecoverPwdFragment = RecoverPwdFragment.newInstance()

            // check is important to prevent activity from attaching the fragment if already its attached
            if (savedInstanceState == null) {
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.recover_pwd_fragment_container, fragment, "recover_pwd_fragment")
                    .commit()
            }
        }

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

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Login via google api
        googleSignInBtn?.setOnClickListener() {
            signInGoogle()
        }

    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            updateUI(currentUser)
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login() {
        val emailText   = email.text.toString()
        val pwdText     = pwd.text.toString()

        if (!validateLoginInput(emailText, pwdText)) {
            return
        }

        loginBtn.isEnabled = false

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

    fun onLoginSuccess(loginBtn: Button) {
        loginBtn.isEnabled = true
        // Go to the home
        //startActivity(Intent(this, ...))
    }

    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }


    private fun validateLoginInput(emailText: String, pwdText: String): Boolean {
        val errorMsg = validateEmail(emailText)
        if (errorMsg.isNotEmpty()) {
            Toast.makeText(applicationContext, errorMsg, Toast.LENGTH_SHORT).show()
            return false
        }

        if (pwdText.isEmpty()) { // Check if the password input is filled
            Toast.makeText(applicationContext, "Enter password", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    companion object {
        private const val TAG = "LoginActivity"
        private const val RC_SIGN_IN = 9001
    }
}
package com.example.vaccination.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.vaccination.Firebase.User
import com.example.vaccination.Fragments.AccountPageFragment
import com.example.vaccination.R
import com.example.vaccination.Utils.OnBackPressedInterface
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDb: DatabaseReference

    var accountLocal: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (supportActionBar != null)
            supportActionBar?.hide()

        setContentView(R.layout.activity_main)


        // Initialize Firebase Auth
        auth = Firebase.auth
        // Initialize Firebase Database
        firebaseDb = Firebase.database.reference

        val uid = auth.currentUser.uid

        firebaseDb.child(User.user_table_name).child(uid).get().addOnSuccessListener {
            Log.i(TAG, "Got user ${uid}")

            accountLocal = it.getValue(User::class.java)

        }.addOnFailureListener{
            Log.e(TAG, "Error getting data", it)
        }

        bottomNavigationView = findViewById(R.id.bottom_nav)

        setUpNavigation()
    }

    private fun setUpNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment!!.navController)
    }

    override fun onBackPressed() {
        val fragment = this.supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
        val currentFragment = fragment?.childFragmentManager?.fragments?.get(0) as? OnBackPressedInterface
        currentFragment?.onBackPressed()?.not()?.let{

            val a = Intent(Intent.ACTION_MAIN)
            a.addCategory(Intent.CATEGORY_HOME)
            a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(a)
        }
        super.onBackPressed()
    }

    companion object {
        private val TAG = "MainActivity"
    }

}
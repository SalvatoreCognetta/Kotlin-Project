package com.example.vaccination.Activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.vaccination.R
import com.example.vaccination.Utils.OnBackPressedInterface
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (supportActionBar != null)
            supportActionBar?.hide()

        setContentView(R.layout.activity_main)
/*
        //Set starting fragment
        val startupFragment = HomeFragment()
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
                .add(R.id., startupFragment, "startupFragmentTag")
                .addToBackStack(null)
                .commit()*/

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
}
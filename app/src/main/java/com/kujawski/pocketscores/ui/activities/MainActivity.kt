package com.kujawski.pocketscores.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kujawski.pocketscores.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            val navController = findNavController(R.id.nav_host_fragment)
            val bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
            bottomNavView.setupWithNavController(navController)
        } catch (e: Exception) {
            Log.e("MainActivity", "Error initializing NavController: ${e.message}")
        }
    }
}

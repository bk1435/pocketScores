package com.kujawski.pocketscores.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kujawski.pocketscores.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
        val navController = navHostFragment?.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        navController?.let {
            NavigationUI.setupWithNavController(bottomNavigationView, it)


            bottomNavigationView.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.teamDetailsFragment -> {

                        navController.navigate(R.id.teamDetailsFragment)
                        true
                    }
                    R.id.aroundLeagueFragment -> {

                        navController.navigate(R.id.aroundLeagueFragment)
                        true
                    }
                    R.id.nav_change_favorite_team -> {

                        navController.navigate(R.id.changeFavoriteTeamFragment)
                        true
                    }
                    else -> false
                }
            }
        }
    }
}

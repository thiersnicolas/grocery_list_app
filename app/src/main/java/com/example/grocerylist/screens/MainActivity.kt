package com.example.grocerylist.screens

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.grocerylist.R
import com.example.grocerylist.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var navController: NavController
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("Main activity created")
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_main_activity) as NavHostFragment
        navController = navHostFragment.navController
        bottomNavigation = binding.bottomNavigation
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        userViewModel.activeUser.observe(this) { foundUser ->
            if (foundUser != null) {
                navController.navigate(MainActivityDirections.actionGlobalNavWelcome())
                setupBottomNav()
            } else {
                navController.navigate(MainActivityDirections.actionGlobalNavUnknownUser())
                clearBottomNav()
            }
        }
    }

    private fun clearBottomNav() {
        bottomNavigation.visibility = View.INVISIBLE
    }

    private fun setupBottomNav() {
        bottomNavigation.visibility = View.VISIBLE
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.logout -> {
                    logout()
                    true
                }

                R.id.groceryListsFragment -> {
                    navController.navigate(R.id.groceryListsFragment)
                    true
                }

                R.id.createGroceryListFragment -> {
                    navController.navigate(R.id.createGroceryListFragment)
                    true
                }

                else -> false
            }
        }
    }

    private fun logout() {
        userViewModel.deleteCurrentUser()
        navController.navigate(MainActivityDirections.actionGlobalNavUnknownUser())
    }
}
package com.silva.lab8

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {
    private lateinit var toolbar: MaterialToolbar
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view_mainActivity) as NavHostFragment
        navController = navHostFragment.navController
        val appBarConfig = AppBarConfiguration(navController.graph)
        toolbar = findViewById(R.id.toolbar_mainActivity)
        toolbar.setupWithNavController(navController, appBarConfig)
        listenToNavGraphChanges()
    }

    private fun listenToNavGraphChanges() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.characterDetailsFragment -> {
                    toolbar.menu.findItem(R.id.menu_character_sort_aToZ).isVisible = false
                    toolbar.menu.findItem(R.id.menu_character_sort_zToA).isVisible = false
                    toolbar.title = "Character Detail"
                }
                R.id.charactersFragment -> {
                    toolbar.menu.findItem(R.id.menu_character_sort_aToZ).isVisible = true
                    toolbar.menu.findItem(R.id.menu_character_sort_zToA).isVisible = true
                    toolbar.title = "Characters"
                }
            }
        }
    }

    fun getToolbar(): MaterialToolbar{
        return toolbar
    }

}
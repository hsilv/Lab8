package com.silva.lab8

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {
    private lateinit var toolbar: MaterialToolbar
    private lateinit var navController: NavController
    private lateinit var appBarConfig: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view_mainActivity) as NavHostFragment
        navController = navHostFragment.navController
        toolbar = findViewById(R.id.toolbar_mainActivity)
        appBarConfig = AppBarConfiguration(setOf(R.id.loginFragment, R.id.charactersFragment))
        toolbar.setupWithNavController(navController, appBarConfig)
        listenToNavGraphChanges()
    }

    private fun listenToNavGraphChanges() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.characterDetailsFragment -> {
                    toolbar.isVisible = true
                    toolbar.menu.clear()
                }
                R.id.charactersFragment -> {
                    toolbar.isVisible = true
                    toolbar.menu.clear()
                    toolbar.inflateMenu(R.menu.menu_characters)
                }
                R.id.loginFragment -> {
                    toolbar.isVisible = false
                }
            }
        }
    }

    fun getToolbar(): MaterialToolbar{
        return toolbar
    }

}
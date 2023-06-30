package com.example.unsplashattestationproject.presentation.bottom_navigation

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.unsplashattestationproject.App
import com.example.unsplashattestationproject.R
import com.example.unsplashattestationproject.data.SharedRepository
import com.example.unsplashattestationproject.databinding.ActivityUnsplashBottomNavigationsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BottomNavigationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUnsplashBottomNavigationsBinding
    lateinit var navigationController: NavController

    @Inject
    lateinit var sharedRepository: SharedRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUnsplashBottomNavigationsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navigationController = getNavController()
        setupBottomNavigation()
        handleIntent(intent)
    }

    override fun onStart() {
        super.onStart()
        observePhotoDownloadComplete()
    }

    private fun setupBottomNavigation() {
        val navView: BottomNavigationView = binding.navView
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.bottom_nav_navigation_photos,
                R.id.bottom_nav_navigation_collections,
                R.id.bottom_nav_navigation_user_profile
            )
        )
        setupActionBarWithNavController(navigationController, appBarConfiguration)
        navView.setupWithNavController(navigationController)
    }

    private fun getNavController(): NavController {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_unsplash_bottom_navigations)
                as NavHostFragment
        return navHostFragment.navController
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        Log.e(TAG, "BottomNavigationActivity: handleIntent: $intent")

        intent.getStringExtra(App.INTENT_KEY_PHOTO_ID)?.let { photoId ->
            val args = Bundle().apply {
                putString(App.INTENT_KEY_PHOTO_ID, photoId)
            }
            navigationController.navigate(R.id.photoDetailsFragment, args)
        }
    }

    private fun observePhotoDownloadComplete() {
        sharedRepository.photoDownloadCompletedEvent.observe(this)
        { downloadResult ->
            Snackbar.make(
                binding.root,
                "Download with ID ${downloadResult.first} completed!",
                Snackbar.LENGTH_INDEFINITE
            )
                .setAction("View") {
                    Log.e(TAG, "observeSharedRepository: COMPLETE_ID ${downloadResult.first}")
                    Log.e(TAG, "observeSharedRepository: COMPLETE_URI ${downloadResult.second}")

                    // Open photo using an external application
                    // todo: implementation
                }
                .show()
        }
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, BottomNavigationActivity::class.java)
        }
    }
}
package com.example.unsplashattestationproject.presentation.bottom_navigation

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.unsplashattestationproject.App
import com.example.unsplashattestationproject.BuildConfig
import com.example.unsplashattestationproject.R
import com.example.unsplashattestationproject.data.SharedRepository
import com.example.unsplashattestationproject.databinding.ActivityUnsplashBottomNavigationsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
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
        sharedRepository.photoDownloadCompletedEvent.observe(
            this,
            ::showDownloadCompleteSnackbar
        )
    }

    private fun showDownloadCompleteSnackbar(downloadResult: Pair<Long, Uri>) {
        Snackbar.make(
            binding.root,
            "Download with ID ${downloadResult.first} completed!",
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(getString(R.string.activity_bottom_navigation_snackbar_view_downloaded_photo)) {
                Log.e(TAG, "observeSharedRepository: COMPLETE_ID ${downloadResult.first}")
                Log.e(TAG, "observeSharedRepository: COMPLETE_URI ${downloadResult.second}")

                viewDownloadedPhoto(downloadResult.second)
            }
            .show()
    }

    private fun viewDownloadedPhoto(fileUri: Uri) {
        val filePath = fileUri.path
        if (filePath == null) {
            Log.e(TAG, "${::viewDownloadedPhoto}: fileUri.path is null")
            return
        }

        val uri = FileProvider.getUriForFile(
            this,
            BuildConfig.DOWNLOADED_FILE_PROVIDER_AUTHORITY, File(filePath)
        )
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "image/*")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }

        startActivity(intent)
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, BottomNavigationActivity::class.java)
        }
    }
}
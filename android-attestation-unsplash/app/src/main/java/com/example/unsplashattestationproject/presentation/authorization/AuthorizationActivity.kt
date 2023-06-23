package com.example.unsplashattestationproject.presentation.authorization

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.unsplashattestationproject.data.network.AuthQuery.Companion.PARAM_CODE
import com.example.unsplashattestationproject.databinding.ActivityAuthorizationBinding
import com.example.unsplashattestationproject.log.TAG
import com.example.unsplashattestationproject.presentation.bottom_navigation.BottomNavigationActivity
import com.example.unsplashattestationproject.presentation.onboarding.OnboardingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthorizationActivity : AppCompatActivity() {

    private val binding: ActivityAuthorizationBinding by lazy {
        ActivityAuthorizationBinding.inflate(layoutInflater)
    }
    private val viewModel: AuthorizationActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (!viewModel.isOnboardingShowed()) {
            openOnboardingActivity()
        }

        observeAuthorizationState()
        setAuthButtonListener()
        handleAuthDeepLink(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { handleAuthDeepLink(it) }
    }

    private fun openOnboardingActivity() {
        startActivity(OnboardingActivity.createIntent(this))
        viewModel.saveOnboardingShowedStatus()
    }

    private fun setAuthButtonListener() {
        binding.activityAuthorizationButtonLogInViaUnsplash.setOnClickListener {
            openBrowserForAuthentication()
        }

        // TODO: remove test button
        binding.activityAuthorizationTestButton.setOnClickListener {
            startActivity(Intent(this, BottomNavigationActivity::class.java))
        }
    }

    private fun openBrowserForAuthentication() {
        val intent = Intent(Intent.ACTION_VIEW, viewModel.composeBrowserAuthUrl())
        startActivity(intent)
    }

    private fun handleAuthDeepLink(intent: Intent) {
        if (intent.action != Intent.ACTION_VIEW)
            return
        val deepLinkUrl = intent.data ?: return

        if (deepLinkUrl.queryParameterNames.contains(PARAM_CODE)) {
            viewModel.authCode = deepLinkUrl.getQueryParameter(PARAM_CODE)
                ?: return

            viewModel.authorizeUser()
        }
    }

    private fun observeAuthorizationState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authorizationState
                    .collect { isAuthorizationSuccess ->
                        handleAuthorizationState(isAuthorizationSuccess)
                    }
            }
        }
    }

    private fun handleAuthorizationState(isAuthorizationSuccess: Boolean) {
        logAndNotifyUser(isAuthorizationSuccess)

        if (isAuthorizationSuccess) {
            startActivity(
                BottomNavigationActivity
                    .createIntent(this@AuthorizationActivity)
            )
            finish()
        }
    }

    private fun logAndNotifyUser(isAuthorizationSuccess: Boolean) {
        with(isAuthorizationSuccess) {
            val authResult = if (this) "success" else "failed. Try again later."

            Log.d(TAG, "Authorization $authResult")
            Toast.makeText(
                this@AuthorizationActivity,
                "Authorization $authResult",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    companion object {
        const val IS_ONBOARDING_SHOWED = "isOnboardingShowed"
    }
}
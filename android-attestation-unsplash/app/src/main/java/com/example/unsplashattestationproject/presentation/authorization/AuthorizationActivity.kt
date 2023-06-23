package com.example.unsplashattestationproject.presentation.authorization

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.unsplashattestationproject.data.network.AuthQuery.Companion.PARAM_CODE
import com.example.unsplashattestationproject.databinding.ActivityAuthorizationBinding
import com.example.unsplashattestationproject.presentation.bottom_navigation.BottomNavigationActivity
import com.example.unsplashattestationproject.presentation.onboarding.OnboardingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthorizationActivity : AppCompatActivity() {

    private val binding: ActivityAuthorizationBinding by lazy {
        ActivityAuthorizationBinding.inflate(layoutInflater)
    }
    private val viewModel : AuthorizationActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (!viewModel.isOnboardingShowed()) {
            openOnboardingActivity()
        }

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

            viewModel.getAccessToken()
        }
    }

    companion object {
        const val IS_ONBOARDING_SHOWED = "isOnboardingShowed"
    }
}
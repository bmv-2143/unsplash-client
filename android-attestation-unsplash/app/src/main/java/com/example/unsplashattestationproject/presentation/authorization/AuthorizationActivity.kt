package com.example.unsplashattestationproject.presentation.authorization

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.example.unsplashattestationproject.databinding.ActivityAuthorizationBinding
import com.example.unsplashattestationproject.log.TAG
import com.example.unsplashattestationproject.presentation.onboarding.OnboardingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthorizationActivity : AppCompatActivity() {

    private val binding: ActivityAuthorizationBinding by lazy {
        ActivityAuthorizationBinding.inflate(layoutInflater)
    }
    private val viewModel : AuthorizationActivityViewModel by viewModels()

    // TODO: add view model, move logic into it

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (!isOnboardingShowed()) {
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
        intent = Intent(this, OnboardingActivity::class.java)
        startActivity(intent)
        saveOnboardingShowedStatus()
    }

    private fun saveOnboardingShowedStatus() {
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(IS_ONBOARDING_SHOWED, true)
        editor.apply()
    }

    private fun setAuthButtonListener() {
        binding.activityAuthorizationButtonLogInViaUnsplash.setOnClickListener {
            openBrowserForAuthentication()
        }
    }

    private fun isOnboardingShowed(): Boolean {
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
        return sharedPreferences.getBoolean(IS_ONBOARDING_SHOWED, false)
    }

    private fun openBrowserForAuthentication() {
        val intent = Intent(Intent.ACTION_VIEW, composeUrl())
        startActivity(intent)
    }
    private fun composeUrl(): Uri =
        Uri.parse(AuthConst.AUTH_URL)
            .buildUpon()
            .appendQueryParameter("client_id",
                AuthConst.CLIENT_ID_ACCESS_KEY)
            .appendQueryParameter("redirect_uri",
                AuthConst.REDIRECT_URL)
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("scope", "public")
            .build()

    private fun handleAuthDeepLink(intent: Intent) {
        if (intent.action != Intent.ACTION_VIEW) return
        val deepLinkUrl = intent.data ?: return
        if (deepLinkUrl.queryParameterNames.contains("code")) {
            val authCode = deepLinkUrl.getQueryParameter("code")
                ?: return


            Log.e(TAG, "handleDeepLink: $authCode")

            //TODO: сохранить/передать authCode для дальнейшего запроса access_token
            viewModel.authCode = authCode

            continueAuthorization()
        }

    }

    private fun continueAuthorization() {
        viewModel.getAccessToken(viewModel.authCode)
    }


    companion object {
        const val IS_ONBOARDING_SHOWED = "isOnboardingShowed"
        const val SHARED_PREFERENCES = "sharedPreferences"
    }
}
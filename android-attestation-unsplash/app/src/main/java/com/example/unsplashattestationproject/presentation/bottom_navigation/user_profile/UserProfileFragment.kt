package com.example.unsplashattestationproject.presentation.bottom_navigation.user_profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.unsplashattestationproject.R
import com.example.unsplashattestationproject.data.dto.profile.UnsplashUserProfile
import com.example.unsplashattestationproject.databinding.FragmentUserProfileBinding
import com.example.unsplashattestationproject.log.TAG
import com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list.PhotoListFragment
import com.example.unsplashattestationproject.presentation.compound.CompoundIconTextView
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UserProfileFragment : Fragment() {

    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!

    private val userProfileViewModel: UserProfileViewModel by viewModels()

    @Inject lateinit var photoListFragmentFactory: PhotoListFragment.Factory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUserProfile()
        userProfileViewModel.loadUserProfile()
    }

    private fun initTabbedLayout(username: String) {
        val adapter = UserProfileTabsAdapter(photoListFragmentFactory, this, username)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(
            binding.fragmentUserProfileTabLayout,
            binding.viewPager
        ) { tab, position ->
            when (position) {
                UserProfileTabsAdapter.FRAGMENT_POSITION_PHOTOS -> tab.text = "123\nPhotos"
                UserProfileTabsAdapter.FRAGMENT_POSITION_LIKED -> tab.text = "44\nLiked"
                else -> tab.text = "1\nCollections"
            }
        }.attach()
    }

    private fun observeUserProfile() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                userProfileViewModel.userProfileFlow.collect { userProfile ->
                    Log.e(TAG, "observeUserProfile: $userProfile")
                    loadAuthorAvatar(userProfile.profileImage.medium)
                    loadUserProfile(userProfile)
                    initTabbedLayout(userProfile.username)
                }
            }
        }
    }

    private fun loadUserProfile(userProfile: UnsplashUserProfile) {
        setFullName(userProfile)
        setOrHide(userProfile.username, binding.fragmentUserProfileUsername)
        setOrHide(userProfile.location, binding.fragmentUserProfileLocation)

        setOrHide(
            getString(
                R.string.fragment_user_profile_twitter_username,
                userProfile.twitterUsername
            ), binding.fragmentUserProfileTwitterUsername
        )
        setOrHide(
            getString(R.string.fragment_user_profile_followed_by, userProfile.followersCount),
            binding.fragmentUserProfileInstargamFollowedBy
        )
        setOrHide(
            getString(
                R.string.fragment_user_profile_instagram_username,
                userProfile.instagramUsername
            ), binding.fragmentUserProfileInstargamUsername
        )
        setOrHide(userProfile.downloads.toString(), binding.fragmentUserProfileDownloads)
    }

    private fun setFullName(userProfile: UnsplashUserProfile) {
        binding.fragmentUserProfileRealName.text = getString(
            R.string.fragment_user_profile_full_name,
            userProfile.firstName,
            userProfile.lastName
        )
    }

    private fun setOrHide(text: String?, textIconView: CompoundIconTextView) {
        if (text.isNullOrEmpty()) {
            textIconView.visibility = View.GONE
        } else {
            textIconView.visibility = View.VISIBLE
            textIconView.text = text
        }
    }

    private fun setOrHide(text: String?, textView: TextView) {
        if (text.isNullOrEmpty()) {
            textView.visibility = View.GONE
        } else {
            textView.visibility = View.VISIBLE
            textView.text = text
        }
    }

    private fun loadAuthorAvatar(avatarUrl: String) {
        Glide.with(binding.root.context)
            .load(avatarUrl)
            .circleCrop()
            .placeholder(R.drawable.photo_list_item_avatar_placeholder)
            .into(binding.fragmentUserProfileImageAvatar)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
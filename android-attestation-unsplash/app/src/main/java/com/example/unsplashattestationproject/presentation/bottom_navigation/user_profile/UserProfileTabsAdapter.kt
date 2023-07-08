package com.example.unsplashattestationproject.presentation.bottom_navigation.user_profile

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.unsplashattestationproject.presentation.bottom_navigation.liked_photos_list.LikedPhotosFragment
import com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list.PhotoListFragment
import com.example.unsplashattestationproject.presentation.onboarding.OnboardingUploadFragment
import javax.inject.Inject

class UserProfileTabsAdapter @Inject constructor(
    private val photoListFragmentFactory: PhotoListFragment.Factory,
    private val parentFragment: Fragment,
    private val username: String
) : FragmentStateAdapter(parentFragment) {

    override fun getItemCount(): Int = NUMBER_OF_FRAGMENTS

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            FRAGMENT_POSITION_PHOTOS -> photoListFragmentFactory.create {
                LinearLayoutManager(parentFragment.requireContext())
            }
            FRAGMENT_POSITION_LIKED -> LikedPhotosFragment.newInstance(username)
            else -> OnboardingUploadFragment.newInstance()
        }
    }

    companion object {
        const val NUMBER_OF_FRAGMENTS = 3
        const val FRAGMENT_POSITION_PHOTOS = 0
        const val FRAGMENT_POSITION_LIKED = 1
    }
}
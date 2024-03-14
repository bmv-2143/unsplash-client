package com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import javax.inject.Inject

class StaggeredLayoutPhotoListFragmentFactory @Inject constructor(
    private val photoListFragmentFactory: PhotoListFragment.Factory
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {

        return when (className) {
            PhotoListFragment::class.java.name -> {
                photoListFragmentFactory.create {
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                }
            }

            else -> super.instantiate(classLoader, className)
        }
    }
}

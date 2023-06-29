package com.example.unsplashattestationproject.presentation.bottom_navigation.photo_details

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.unsplashattestationproject.R
import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhotoDetails
import com.example.unsplashattestationproject.databinding.FragmentPhotoDetailsBinding
import com.example.unsplashattestationproject.presentation.bottom_navigation.BottomNavigationActivityViewModel
import com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list.PhotoItemLoader
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class PhotoDetailsFragment : Fragment() {

    private var _binding: FragmentPhotoDetailsBinding? = null
    private val binding get() = _binding!!
    private val activityViewModel: BottomNavigationActivityViewModel by activityViewModels()

    private val photoDetailsFragmentViewModel: PhotoDetailsFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoDetailsBinding.inflate(inflater, container, false)
        loadPhotoItemData()
        return binding.root
    }

    private fun loadPhotoItemData() {
        if (activityViewModel.selectedPhoto != null) {
            PhotoItemLoader(binding.photoItem).loadData(activityViewModel.selectedPhoto!!)
        } else {
            Log.e(TAG, "onCreateView: selectedPhoto is null")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners()
        observerPhotoDetails()
        photoDetailsFragmentViewModel.loadPhotoDetails(activityViewModel.selectedPhoto!!.remoteId)
        addActionBarMenu()
    }

    private fun setClickListeners() {
        binding.fragmentPhotoDetailsLocation.setOnClickListener {
            Toast.makeText(requireContext(), "Location", Toast.LENGTH_SHORT).show()
        }
        binding.fragmentPhotoDetailsDownloadBtn.setOnClickListener {
            Toast.makeText(requireContext(), "Download", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observerPhotoDetails() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                photoDetailsFragmentViewModel.photoDetailsFlow.collect { photoDetails ->
                    Log.e(TAG, "PHOTO DETAILS: $photoDetails")
                    updateLocation(photoDetails)
                    updateTags(photoDetails)
                    updateExif(photoDetails)
                    updateAboutAuthor(photoDetails)
                    updateDownloadCount(photoDetails)
                }
            }
        }
    }

    private fun updateLocation(photoDetails: UnsplashPhotoDetails) {
        updateLocationVisibility(photoDetails)
        setCityCountryText(photoDetails)
    }

    private fun setCityCountryText(photoDetails: UnsplashPhotoDetails) {
        val cityCountry = listOfNotNull(photoDetails.location?.city, photoDetails.location?.country)
        binding.fragmentPhotoDetailsLocationText.text =
            cityCountry.joinToString(separator = ", ")
    }

    private fun updateLocationVisibility(photoDetails: UnsplashPhotoDetails) {
        binding.fragmentPhotoDetailsLocation.visibility =
            if (photoDetails.location?.city == null && photoDetails.location?.country == null) {
                View.GONE
            } else {
                View.VISIBLE
            }
    }

    private fun updateTags(photoDetails: UnsplashPhotoDetails) {
        binding.fragmentPhotoDetailsTags.text =
            photoDetails.tags?.map { it.title }?.joinToString { "#$it" }
    }

    private fun updateExif(photoDetails: UnsplashPhotoDetails) {
        updateTextOrHide(binding.fragmentPhotoDetailsExifMadeWith, photoDetails.exif?.make,
            R.string.fragment_photo_details_exif_made_with)
        updateTextOrHide(binding.fragmentPhotoDetailsExifModel, photoDetails.exif?.model,
            R.string.fragment_photo_details_exif_model)
        updateTextOrHide(binding.fragmentPhotoDetailsExifExposure, photoDetails.exif?.exposureTime,
            R.string.fragment_photo_details_exif_exposure)
        updateTextOrHide(binding.fragmentPhotoDetailsExifAperture, photoDetails.exif?.aperture,
            R.string.fragment_photo_details_exif_aperture)
        updateTextOrHide(binding.fragmentPhotoDetailsExifFocalLength, photoDetails.exif?.focalLength,
            R.string.fragment_photo_details_exif_focal_length)
        updateTextOrHide(binding.fragmentPhotoDetailsExifIso, photoDetails.exif?.iso?.toString(),
            R.string.fragment_photo_details_exif_iso)
    }

    private fun updateTextOrHide(textView : TextView, textToSet : String?, patternId : Int) {
        if (textToSet == null) {
            textView.visibility = View.GONE
        } else {
            textView.text = getString(patternId, textToSet)
        }
    }

    private fun updateAboutAuthor(photoDetails: UnsplashPhotoDetails) {
        binding.fragmentPhotoDetailsAboutAuthor.text = getString(
            R.string.fragment_photo_details_about_author,
            photoDetails.user.username,
            photoDetails.user.bio ?: "",
        )
    }

    private fun updateDownloadCount(photoDetails: UnsplashPhotoDetails) {
        binding.fragmentPhotoDetailsDownloadBtnText.text = getString(
            R.string.fragment_photo_details_download_btn_text, photoDetails.downloads)
    }

    private fun addActionBarMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.photo_details_fragment_actionbar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_share -> {
                        sharePhoto()
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun sharePhoto() {
        activityViewModel.selectedPhoto?.let { photo ->
            val photoId = photo.remoteId
            val link = "https://unsplash.com/photos/$photoId"
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, link)
            }
            startActivity(
                Intent.createChooser(
                    shareIntent,
                    getString(R.string.menu_action_share_photo)
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
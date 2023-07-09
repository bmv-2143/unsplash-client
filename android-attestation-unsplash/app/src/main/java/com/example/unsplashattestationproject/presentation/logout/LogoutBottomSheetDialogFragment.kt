package com.example.unsplashattestationproject.presentation.logout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.unsplashattestationproject.databinding.FragmentBottomSheetDialogLogoutBinding
import com.example.unsplashattestationproject.presentation.bottom_navigation.BottomNavigationActivityViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LogoutBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetDialogLogoutBinding? = null
    private val binding get() = _binding!!

    private val activityViewModel: BottomNavigationActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetDialogLogoutBinding.inflate(inflater, container, false)
        setButtonClickListeners()
        return binding.root
    }

    private fun setButtonClickListeners() {
        binding.fragmentBottomSheetDialogButtonNo.setOnClickListener {
            dismiss()
        }
        binding.fragmentBottomSheetDialogButtonYes.setOnClickListener {
            activityViewModel.logout()
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
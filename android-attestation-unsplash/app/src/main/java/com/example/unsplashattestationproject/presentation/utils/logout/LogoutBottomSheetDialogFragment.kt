package com.example.unsplashattestationproject.presentation.utils.logout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.unsplashattestationproject.databinding.FragmentBottomSheetDialogLogoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LogoutBottomSheetDialogFragment(private val onLogoutClicked: () -> Unit) :
    BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetDialogLogoutBinding? = null
    private val binding get() = _binding!!

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
            onLogoutClicked()
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
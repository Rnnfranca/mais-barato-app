package com.example.maisbarato.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.maisbarato.R
import com.example.maisbarato.databinding.BottomSheetInfoUsuarioBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetMaisInfo : BottomSheetDialogFragment() {

    private var _binding: BottomSheetInfoUsuarioBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetInfoUsuarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.icEdit.setOnClickListener {
            findNavController().navigate(R.id.userProfileFragment)
            dialog?.dismiss()
        }

        binding.tvTrocarFoto.setOnClickListener {
            findNavController().navigate(R.id.userProfileFragment)
            dialog?.dismiss()
        }
    }

}
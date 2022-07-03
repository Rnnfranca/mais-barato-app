package com.example.maisbarato.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.example.maisbarato.databinding.BottomSheetInfoUsuarioBinding
import com.example.maisbarato.viewmodel.BottomSheetMaisInfoViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetMaisInfo : BottomSheetDialogFragment() {

    private var _binding: BottomSheetInfoUsuarioBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BottomSheetMaisInfoViewModel by viewModels()

    private var launcher: ActivityResultLauncher<String> = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it?.also { uri ->
            viewModel.salvaURLImagem(uri)
        }
    }

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

        binding.tvTrocarFoto.setOnClickListener {
            launcher.launch("image/*")
        }
    }

}
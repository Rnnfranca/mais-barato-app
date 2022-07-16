package com.example.maisbarato.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.maisbarato.databinding.FragmentUserProfileBinding
import com.example.maisbarato.viewmodel.UserProfileViewModel
import kotlinx.coroutines.launch

class UserProfileFragment : Fragment() {

    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserProfileViewModel by viewModels()

    private var launcher: ActivityResultLauncher<String> = registerForActivityResult(
        ActivityResultContracts.GetContent()) {
        it?.also { uri ->
            viewModel.salvaURLImagem(uri)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getImageUrl()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.imageUserUrl.collect { imgUserUrl ->

                Glide.with(binding.imageUser)
                    .load(imgUserUrl)
                    .into(binding.imageUser)
            }
        }

        binding.cardViewImageUser.setOnClickListener {
            launcher.launch("image/*")
        }

        binding.buttonUpdate.setOnClickListener {

        }
    }
}
package com.example.maisbarato.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.maisbarato.R
import com.example.maisbarato.databinding.FragmentUserProfileBinding
import com.example.maisbarato.util.StateViewResult
import com.example.maisbarato.util.ValidateUtil
import com.example.maisbarato.viewmodel.UserProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserProfileFragment : Fragment() {

    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserProfileViewModel by viewModels()

    private var launcher: ActivityResultLauncher<String> = registerForActivityResult(
        ActivityResultContracts.GetContent()) {
        it?.also { uri ->
            viewModel.setImageUri(uri.toString())
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
        viewModel.getInfoUser()

        flowCollectors()
        clickListeners()
    }

    private fun flowCollectors() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stateView.collect { stateView ->
                when (stateView) {
                    is StateViewResult.Loading -> {
                        binding.buttonUpdate.visibility = View.GONE
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is StateViewResult.Success -> {
                        binding.buttonUpdate.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        findNavController().navigate(R.id.listaOfertasFragment)
                    }
                    is StateViewResult.Error -> {
                        binding.buttonUpdate.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), getString(R.string.falha_atualizacao_user), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userInfo.collect {
                it?.also { usuario ->
                    binding.tieUserName.setText(usuario.nome)
                    binding.tieEmail.setText(usuario.email)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.imageUserUrl.collect { imgUserUrl ->

                if (imgUserUrl.isNullOrEmpty()) {
                    Glide.with(binding.imageUser)
                        .load(R.drawable.profile_picture_default)
                        .into(binding.imageUser)
                } else {
                    Glide.with(binding.imageUser)
                        .load(imgUserUrl)
                        .into(binding.imageUser)
                }
            }
        }
    }

    private fun clickListeners() {
        binding.containerUpdateImage.setOnClickListener {
            launcher.launch("image/*")
        }

        binding.tieUserName.doAfterTextChanged {
            it?.toString()?.also { texto ->
                binding.tilUserName.error = if (ValidateUtil.validaNome(texto)) {
                    getString(R.string.digite_nome_completo)
                } else {
                    null
                }
            }
        }

        binding.tieEmail.doAfterTextChanged {
            it?.toString()?.also { texto ->
                binding.tilEmail.error = if (ValidateUtil.validaEmail(texto)) {
                    getString(R.string.email_invalido)
                } else {
                    null
                }
            }
        }

        binding.buttonUpdate.setOnClickListener {

            if (!binding.tieUserName.text.isNullOrEmpty() && !binding.tieEmail.text.isNullOrEmpty()) {
                viewModel.updateInfo(
                    binding.tieUserName.text.toString(),
                    binding.tieEmail.text.toString(),
                )
            }
        }
    }
}
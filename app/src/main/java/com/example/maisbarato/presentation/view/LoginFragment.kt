package com.example.maisbarato.presentation.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.maisbarato.R
import com.example.maisbarato.databinding.FragmentLoginBinding
import com.example.maisbarato.util.StateViewResult
import com.example.maisbarato.viewmodel.LoginViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    private val TAG = LoginFragment::class.java.name

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        viewModel.getDadosLogin()
        viewModel.getSwitchLoginStatus()

        observers()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.editTextEmailLogin.doAfterTextChanged {
            binding.inputEmailLogin.error = null
        }

        binding.editTextSenhaLogin.doAfterTextChanged {
            binding.inputSenhaLogin.error = null
        }

        binding.btnEntrar.setOnClickListener {

            val email = binding.editTextEmailLogin.text
            val senha = binding.editTextSenhaLogin.text

            binding.inputEmailLogin.error = if(email.isNullOrEmpty()) {
                getString(R.string.preencha_email)
            } else {
                null
            }

            binding.inputSenhaLogin.error = if(senha.isNullOrEmpty()) {
                getString(R.string.preencha_senha)
            } else {
                null
            }

            if (!email.isNullOrEmpty() && !senha.isNullOrEmpty()) {
                viewModel.login(email.toString(), senha.toString())
            }
        }

        binding.btnCriarConta.setOnClickListener {
            val actions = LoginFragmentDirections.actionLoginFragmentToCadastroFragment()
            findNavController().navigate(actions)
        }
    }

    private fun observers() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateView.collect { stateViewResult ->

                    when (stateViewResult) {
                        is StateViewResult.Success -> {
                            binding.progressBar.visibility = View.GONE

                            if (binding.switchLembrarLogin.isChecked) {
                                viewModel.salvaDadosLogin(
                                    binding.editTextEmailLogin.text.toString(),
                                    binding.editTextSenhaLogin.text.toString()
                                )
                            } else {
                                viewModel.limpaLoginSalvo()
                            }

                            viewModel.salvaSwitchLoginStatus(binding.switchLembrarLogin.isChecked)

                            val actions = LoginFragmentDirections.actionLoginFragmentToListaOfertasFragment()
                            findNavController().navigate(actions)

                        }
                        is StateViewResult.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.btnEntrar.visibility = View.VISIBLE

                            binding.btnEsqueciSenha.isClickable = true
                            binding.btnCriarConta.isClickable = true

                            Log.e(TAG, stateViewResult.errorMsg)
                            MaterialAlertDialogBuilder(requireContext())
                                .setTitle(getString(R.string.falha_autenticacao))
                                .setMessage(getString(R.string.usuario_senha_invalidos))
                                .setNeutralButton("Ok") { dialog, _ ->
                                    dialog.dismiss()
                                }
                                .show()

                        }
                        is StateViewResult.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.btnEntrar.visibility = View.INVISIBLE

                            binding.btnEsqueciSenha.isClickable = false
                            binding.btnCriarConta.isClickable = false
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginUsuario.collect { stateViewResult ->
                    when (stateViewResult) {
                        is StateViewResult.Success -> {
                            binding.editTextEmailLogin.setText(stateViewResult.result?.email)
                            binding.editTextSenhaLogin.setText(stateViewResult.result?.password)
                        }
                        is StateViewResult.Error -> {
                            Toast.makeText(requireContext(), stateViewResult.errorMsg, LENGTH_LONG).show()
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.switchStatus.collect { stateViewResult ->
                    when (stateViewResult) {
                        is StateViewResult.Success -> {
                            binding.switchLembrarLogin.isChecked = stateViewResult.result ?: false
                        }
                        is StateViewResult.Error -> {
                            Toast.makeText(requireContext(), stateViewResult.errorMsg, LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
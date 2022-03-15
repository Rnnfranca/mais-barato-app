package com.example.maisbarato.view

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
import androidx.navigation.fragment.findNavController
import com.example.maisbarato.databinding.FragmentLoginBinding
import com.example.maisbarato.util.StateViewResult
import com.example.maisbarato.viewmodel.LoginViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val TAG = "CadastroFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        viewModel.getLogin()
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
                "Preencha o e-mail"
            } else {
                null
            }

            binding.inputSenhaLogin.error = if(senha.isNullOrEmpty()) {
                "Preencha a senha"
            } else {
                null
            }

            if (!email.isNullOrEmpty() && !senha.isNullOrEmpty()) {

                viewModel.login(email.toString(), senha.toString())

            }


        }

        binding.textCriarConta.setOnClickListener {
            val actions = LoginFragmentDirections.actionLoginFragmentToCadastroFragment()
            findNavController().navigate(actions)
        }
    }

    private fun observers() {

        viewModel.stateView.observe(viewLifecycleOwner) { singleLiveEvent ->
            singleLiveEvent.getContentIfNotHandled()?.also { stateViewResult ->

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

                        binding.textEsqueciSenha.isClickable = true
                        binding.textCriarConta.isClickable = true

                        Log.e(TAG, stateViewResult.errorMsg)
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Falha na autenticação")
                            .setMessage("Usuário e/ou senha inválido")
                            .setNeutralButton("Ok") { dialog, which ->

                            }
                            .show()

                    }
                    is StateViewResult.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.btnEntrar.visibility = View.INVISIBLE

                        binding.textEsqueciSenha.isClickable = false
                        binding.textCriarConta.isClickable = false
                    }
                }
            }

        }

        viewModel.loginUsuario.observe(viewLifecycleOwner) { singleLiveEvent ->
            singleLiveEvent.getContentIfNotHandled()?.also {  stateViewResult ->

                when (stateViewResult) {
                    is StateViewResult.Success -> {
                        binding.editTextEmailLogin.setText(stateViewResult.result.email)
                        binding.editTextSenhaLogin.setText(stateViewResult.result.password)
                    }
                    is StateViewResult.Error -> {
                        Toast.makeText(requireContext(), stateViewResult.errorMsg, LENGTH_LONG).show()
                    }
                }
            }
        }

        viewModel.switchStatus.observe(viewLifecycleOwner) { singleLiveEvent ->
            singleLiveEvent.getContentIfNotHandled()?.also { stateViewResult ->

                when (stateViewResult) {
                    is StateViewResult.Success -> {
                        binding.switchLembrarLogin.isChecked = stateViewResult.result
                    }
                    is StateViewResult.Error -> {
                        Toast.makeText(requireContext(), stateViewResult.errorMsg, LENGTH_LONG).show()
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
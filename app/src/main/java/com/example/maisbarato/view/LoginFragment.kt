package com.example.maisbarato.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.maisbarato.databinding.FragmentLoginBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val TAG = "CadastroFragment"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
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

            binding.inputEmailLogin.error = if(binding.editTextEmailLogin.text.isNullOrEmpty()) {
                "Preencha o e-mail"
            } else {
                null
            }

            binding.inputSenhaLogin.error = if(binding.editTextSenhaLogin.text.isNullOrEmpty()) {
                "Preencha a senha"
            } else {
                null
            }

            if (!binding.editTextEmailLogin.text.isNullOrEmpty() && !binding.editTextSenhaLogin.text.isNullOrEmpty()) {
                auth.signInWithEmailAndPassword(
                    binding.editTextEmailLogin.text.toString(),
                    binding.editTextSenhaLogin.text.toString()
                ).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val actions = LoginFragmentDirections.actionLoginFragmentToListaOfertasFragment()
                        findNavController().navigate(actions)
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Falha na autenticação")
                            .setMessage("Usuário e/ou senha inválido")
                            .setNeutralButton("Ok") { dialog, which ->

                            }
                            .show()
                    }
                }
            }


        }

        binding.textCriarConta.setOnClickListener {
            val actions = LoginFragmentDirections.actionLoginFragmentToCadastroFragment()
            findNavController().navigate(actions)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
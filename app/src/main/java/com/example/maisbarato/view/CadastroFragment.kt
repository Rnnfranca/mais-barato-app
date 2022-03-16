package com.example.maisbarato.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.maisbarato.R
import com.example.maisbarato.databinding.FragmentCadastroBinding
import com.example.maisbarato.viewmodel.CadastroViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

class CadastroFragment : Fragment() {

    private var _binding: FragmentCadastroBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CadastroViewModel by viewModels()

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val TAG = "CadastroFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currentUser = auth.currentUser

        if(currentUser != null) {
            //reload()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCadastroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnCadastrar.setOnClickListener {
            cadastrarUsuario()
        }

        binding.textEntrar.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.editTextNomeCadastro.doAfterTextChanged {
            it?.toString()?.also { texto ->
                binding.inputNomeCadastro.error = if (validaNome(texto)) {
                    "Digite seu nome completo"
                } else {
                    null
                }
            }
        }

        binding.editTextEmailCadastro.doAfterTextChanged {
            it?.toString()?.also { texto ->
                binding.inputEmailCadastro.error = if (validaEmail(texto)) {
                    "E-mail inválido"
                } else {
                    null
                }
            }
        }

        binding.editTextSenhaCadastro.doAfterTextChanged {
            it?.toString()?.also { texto ->
                binding.inputSenhaCadastro.error = if (validaSenha(texto)) {
                    "Senha inválida"
                } else {
                    null
                }
            }
        }
    }

    private fun cadastrarUsuario() {
        auth.createUserWithEmailAndPassword(
            binding.editTextEmailCadastro.text.toString(),
            binding.editTextSenhaCadastro.text.toString()
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "createUserWithEmail:success")

                findNavController().navigate(R.id.action_cadastroFragment_to_listaOfertasFragment)

                enviaEmailVerificacao(task)
            } else {
                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                Toast.makeText(
                    requireContext(), "Authentication failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun enviaEmailVerificacao(task: Task<AuthResult>) {
        val user = task.result?.user

        user?.sendEmailVerification()
            ?.addOnCompleteListener { taskEmailVerification ->
                if (taskEmailVerification.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        "Verification email sent to " + user.email,
                        Toast.LENGTH_SHORT
                    ).show()

                    viewModel.salvaUIDUsuario(user.uid)

                } else {
                    Log.e(TAG, "sendEmailVerification", taskEmailVerification.exception);
                    Toast.makeText(
                        requireContext(),
                        "Failed to send verification email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun validaEmail(email: String): Boolean {
        return !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun validaNome(nome: String): Boolean {
        return nome.length < 3 || !nome.contains(" ")
    }

    private fun validaSenha(senha: String): Boolean {

        val pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$")
        val matcher = pattern.matcher(senha)

        return !matcher.matches()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
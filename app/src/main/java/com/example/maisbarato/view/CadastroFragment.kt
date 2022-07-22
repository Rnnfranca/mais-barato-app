package com.example.maisbarato.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.maisbarato.R
import com.example.maisbarato.databinding.FragmentCadastroBinding
import com.example.maisbarato.util.StateViewResult
import com.example.maisbarato.util.ValidateUtil
import com.example.maisbarato.viewmodel.CadastroViewModel
import kotlinx.coroutines.launch

class CadastroFragment : Fragment() {

    private var _binding: FragmentCadastroBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CadastroViewModel by viewModels()

    private val TAG = CadastroFragment::class.java.name

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCadastroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        listeners()
        observers()
    }

    private fun listeners() {
        binding.btnCadastrar.setOnClickListener {
            viewModel.criarUsuario(
                binding.editTextNomeCadastro.text.toString(),
                binding.editTextEmailCadastro.text.toString(),
                binding.editTextSenhaCadastro.text.toString()
            )
        }

        binding.textEntrar.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.editTextNomeCadastro.doAfterTextChanged {
            it?.toString()?.also { texto ->
                binding.inputNomeCadastro.error = if (ValidateUtil.validaNome(texto)) {
                    getString(R.string.digite_nome_completo)
                } else {
                    null
                }
            }
        }

        binding.editTextEmailCadastro.doAfterTextChanged {
            it?.toString()?.also { texto ->
                binding.inputEmailCadastro.error = if (ValidateUtil.validaEmail(texto)) {
                    getString(R.string.email_invalido)
                } else {
                    null
                }
            }
        }

        binding.editTextSenhaCadastro.doAfterTextChanged {
            it?.toString()?.also { texto ->
                binding.inputSenhaCadastro.error = if (ValidateUtil.validaSenha(texto)) {
                    getString(R.string.senha_invalida)
                } else {
                    null
                }
            }
        }
    }

    private fun observers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateView.collect { stateView ->

                    when (stateView) {
                        is StateViewResult.Loading -> {
                            binding.btnCadastrar.visibility = View.INVISIBLE
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is StateViewResult.Success -> {
                            binding.progressBar.visibility = View.INVISIBLE

                            findNavController().navigate(R.id.action_cadastroFragment_to_listaOfertasFragment)
                        }

                        is StateViewResult.Error -> {
                            binding.progressBar.visibility = View.INVISIBLE

                            Toast.makeText(
                                requireContext(), getString(R.string.falha_autenticacao),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                }
            }


            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.emailAutenticationState.collect { stateView ->

                    when (stateView) {
                        is StateViewResult.Success -> {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.email_verificacao_enviado) + stateView,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is StateViewResult.Error -> {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.falha_enviar_email_verificacao),
                                Toast.LENGTH_SHORT
                            ).show()
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
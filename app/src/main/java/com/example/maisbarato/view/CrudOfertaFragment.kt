package com.example.maisbarato.view

import android.content.ContentResolver
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.maisbarato.databinding.FragmentCrudOfertaBinding
import com.example.maisbarato.model.Oferta
import com.example.maisbarato.util.getCurrentTime
import com.example.maisbarato.view.adapter.ListaImagemAdapter
import com.example.maisbarato.viewmodel.CrudOfertaViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.util.*

@AndroidEntryPoint
class CrudOfertaFragment : Fragment() {

    private var _binding: FragmentCrudOfertaBinding? = null
    private val binding get() = _binding!!

    private val crudViewModel: CrudOfertaViewModel by viewModels()
    private lateinit var launcher: ActivityResultLauncher<String>
    private lateinit var recyclerView: RecyclerView
    private lateinit var listaImagemAdapter: ListaImagemAdapter

    private lateinit var contentResolver: ContentResolver

    private var listaImagens = listOf<String?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCrudOfertaBinding.inflate(inflater, container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configuracaoView()
        configuracaoSelecaoImagem()
        listeners()
    }

    private fun configuracaoSelecaoImagem() {
        launcher = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uri ->
            uri?.also { listUri ->

                listaImagemAdapter.atualizaListaImagens(listUri)

                listaImagens = listUri.mapNotNull { it.toString() }


                if (listUri.isNotEmpty()) {
                    binding.imagemOferta.visibility = View.INVISIBLE
                    binding.botaoTrocarImagens.visibility = View.VISIBLE
                } else {
                    binding.imagemOferta.visibility = View.VISIBLE
                    binding.botaoTrocarImagens.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun configuracaoView() {
        recyclerView = binding.recyclerViewImagens
        recyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        listaImagemAdapter = ListaImagemAdapter(listOf())
        recyclerView.adapter = listaImagemAdapter

        contentResolver = requireContext().contentResolver
    }

    private fun listeners() {
        binding.imagemOferta.setOnClickListener {
            abrirSelecaoImagem()
        }

        binding.btnSalvar.setOnClickListener {


            if (validaCampos()) {
                salvaOferta()
            } else {
                dialogValidaCampos()
            }
        }

        binding.botaoTrocarImagens.setOnClickListener {
            abrirSelecaoImagem()
        }

        var checkedItem = 0
        binding.editTextEstabelecimento.setOnClickListener {

            val singleItems = arrayOf("Muffato", "Amigão", "Bandeirantes")
            var selecteditem = ""


            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Estabelecimentos")
                .setNeutralButton("Cancelar") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("Selecionar") { dialog, which ->
                    binding.editTextEstabelecimento.setText(selecteditem)
                    dialog.dismiss()
                }
                .setSingleChoiceItems(singleItems, checkedItem) { dialog, which ->
                    selecteditem = singleItems[which]
                    checkedItem = which
                    Log.d("CrudFragment", "$which")
                }
                .show()
        }

        binding.editTextPrecoAntigo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            private var precoAntigo = ""
            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != precoAntigo) {
                    precoAntigo = formataTextoPreco(s)
                    binding.editTextPrecoAntigo.setText(precoAntigo)
                    binding.editTextPrecoAntigo.setSelection(precoAntigo.length)
                }
            }
        })

        binding.editTextPrecoNovo.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            private var precoNovo = ""
            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != precoNovo) {
                    precoNovo = formataTextoPreco(s)
                    binding.editTextPrecoNovo.setText(precoNovo)
                    binding.editTextPrecoNovo.setSelection(precoNovo.length)
                }
            }
        })

        binding.editTextTitulo.doAfterTextChanged {
            it?.toString()?.also { texto ->
                binding.crudTitulo.error = if(texto.length < 10 ) {
                    "Mínimo 10 caracteres"
                } else {
                    null
                }
            }
        }

        binding.editTextInfoAdicionais.doAfterTextChanged {

            it?.toString()?.also { texto ->
                binding.crudInfoAdicionais.error = if(texto.length < 10) {
                    "Mínimo 10 caracteres"
                } else {
                    null
                }
            }
        }
    }

    private fun abrirSelecaoImagem() {
        launcher.launch("image/*")
    }

    private fun dialogValidaCampos() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage("Preencha os campos obrigatórios")
            .setNeutralButton("Confirmar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun validaCampos(): Boolean {

        return listaImagens.isNotEmpty() &&
                binding.editTextTitulo.text?.isNotEmpty() == true &&
                binding.editTextEstabelecimento.text?.isNotEmpty() == true &&
                binding.editTextPrecoNovo.text?.isNotEmpty() == true &&
                binding.editTextInfoAdicionais.text?.isNotEmpty() == true
    }

    private fun formataTextoPreco(s: Editable?): String {

        val cleanString: String = s?.toString()?.replace("""[R$,.]""".toRegex(), "")
            ?.replace("\\s".toRegex(), "") ?: ""

        val parsed = cleanString.toDouble()
        return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format((parsed / 100))

    }

    private fun salvaOferta() {

        val oferta = Oferta(
            listaUrlImagem = listaImagens,
            titulo = binding.editTextTitulo.text.toString(),
            nomeLoja = binding.editTextEstabelecimento.text.toString(),
            valorAntigo = formatarPrecoParaSalvar(binding.editTextPrecoAntigo.text?.toString()),
            valorNovo = formatarPrecoParaSalvar(binding.editTextPrecoNovo.text?.toString()),
            dataInclusao = getCurrentTime(),
            descricao = binding.editTextInfoAdicionais.text.toString()
        )

        crudViewModel.adicionaOferta(oferta)
    }

    private fun formatarPrecoParaSalvar(preco: String?): Double? {

        return preco?.replace("\\s".toRegex(),"")
            ?.replace("R$","")
            ?.replace(",", ".")
            ?.toDoubleOrNull()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
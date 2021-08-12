package com.example.maisbarato.view

import android.content.ContentResolver
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.maisbarato.databinding.FragmentCrudOfertaBinding
import com.example.maisbarato.model.Oferta
import com.example.maisbarato.viewmodel.CrudOfertaViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CrudOfertaFragment : Fragment() {

    private var _binding: FragmentCrudOfertaBinding? = null
    private val binding get() = _binding!!

    private val crudViewModel: CrudOfertaViewModel by viewModels()
    private lateinit var launcher: ActivityResultLauncher<String>

    private lateinit var contentResolver: ContentResolver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contentResolver = requireContext().contentResolver

        launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.also {
                Glide.with(binding.imagemOferta)
                    .load(uri)
                    .into(binding.imagemOferta)
            }
        }

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

        binding.imagemOferta.setOnClickListener {
            launcher.launch("image/*")
        }

        binding.btnSalvar.setOnClickListener {
            salvaOferta()
        }

    }



    private fun salvaOferta() {
        Oferta(
            imagem = binding.imagemOferta.drawable.toBitmap(),
            titulo = binding.editTextTitulo.text.toString(),
            nomeLoja = binding.editTextEstabelecimento.text.toString(),
            fotoLoja = binding.imagemOferta.drawable.toBitmap(),
            valorAntigo = 199.0,
            valorNovo = 99.9,
            dataInclusao = "09/08/2021",
            descricao = binding.editTextInfoAdicionais.text.toString()
        ).apply {
            crudViewModel.adicionaOferta(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
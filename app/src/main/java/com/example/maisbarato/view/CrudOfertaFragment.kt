package com.example.maisbarato.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.maisbarato.R
import com.example.maisbarato.databinding.FragmentCrudOfertaBinding
import com.example.maisbarato.model.Oferta
import com.example.maisbarato.viewmodel.CrudOfertaViewModel

class CrudOfertaFragment : Fragment() {

    private var _binding: FragmentCrudOfertaBinding? = null
    private val binding get() = _binding!!

    private lateinit var crudViewModel: CrudOfertaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        crudViewModel = ViewModelProvider(this).get(CrudOfertaViewModel::class.java)
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

        binding.btnSalvar.setOnClickListener {
            salvaOferta()
        }
    }

    private fun salvaOferta() {
        Oferta(
            imagem = binding.imagemOferta.drawable.toBitmap(),
            titulo = binding.editTextTitulo.text.toString(),
            nomeLoja = binding.editTextEstabelecimento.text.toString(),
            fotoLoja = resources.getDrawable(R.drawable.air_fryer).toBitmap(),
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
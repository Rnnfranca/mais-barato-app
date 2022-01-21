package com.example.maisbarato.view

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.maisbarato.databinding.FragmentDetalhesOfertaBinding
import com.example.maisbarato.model.Oferta
import com.example.maisbarato.view.adapter.ListaImagemAdapter
import java.text.SimpleDateFormat
import java.util.*

class DetalhesOfertaFragment : Fragment() {

    private var _binding: FragmentDetalhesOfertaBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var listaImagemAdapter: ListaImagemAdapter

    private val args: DetalhesOfertaFragmentArgs by navArgs()
    private var oferta: Oferta? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        oferta = args.oferta
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetalhesOfertaBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.recyclerViewImagens
        recyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        listaImagemAdapter = ListaImagemAdapter(listOf())
        recyclerView.adapter = listaImagemAdapter


        oferta?.also { oferta ->

            val data = Date(oferta.dataInclusao)
            val formatDate = SimpleDateFormat("dd/MM/yyyy")
            val dateText = formatDate.format(data)

            listaImagemAdapter.atualizaListaImagens(oferta.listaUrlImagem)

            binding.tituloPromocao.text = oferta.titulo
            binding.dataInclusao.text = dateText
            binding.nomeLoja.text = oferta.nomeLoja
            binding.precoAntigo.paintFlags = binding.precoAntigo.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            binding.precoAntigo.text = oferta.valorAntigo?.toString()
            binding.precoNovo.text = oferta.valorNovo.toString()
            binding.descricaoOferta.text = oferta.descricao
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
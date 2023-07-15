package com.example.maisbarato.presentation.view

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.maisbarato.R
import com.example.maisbarato.databinding.FragmentDetalhesOfertaBinding
import com.example.maisbarato.data.model.Oferta
import com.example.maisbarato.view.adapter.ListaImagemAdapter
import com.example.maisbarato.viewmodel.DetalhesOfertaViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class DetalhesOfertaFragment : Fragment() {

    private var _binding: FragmentDetalhesOfertaBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetalhesOfertaViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var listaImagemAdapter: ListaImagemAdapter

    private val args: DetalhesOfertaFragmentArgs by navArgs()
    private var oferta: Oferta? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        oferta = args.oferta
        oferta?.also {
            viewModel.getUserOffersInfos(it.userUid)
        }
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
        configRecyclerViewImages()
        configOffer()
        configFavoriteButton()
    }

    private fun configFavoriteButton() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isFavorite.collect { isFavorite ->
                if (isFavorite) {
                    binding.botaoAdicionarFavoritos.setIconResource(R.drawable.ic_favorite_filled)
                    binding.botaoAdicionarFavoritos.text = getString(R.string.remover_dos_favoritos)
                } else {
                    binding.botaoAdicionarFavoritos.setIconResource(R.drawable.ic_favorite)
                    binding.botaoAdicionarFavoritos.text =
                        getString(R.string.adicionar_aos_favoritos)
                }
            }
        }
    }

    private fun configOffer() {

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.userInfo.collect { usuario ->
                    oferta?.also { oferta ->

                        val data = Date(oferta.dataInclusao)
                        val formatDate = SimpleDateFormat("dd/MM/yyyy")
                        val dateText = formatDate.format(data)

                        viewModel.verifyFavorite(ofertaId = oferta.id)

                        listaImagemAdapter.atualizaListaImagens(oferta.listaUrlImagem)

                        binding.tituloPromocao.text = oferta.titulo
                        binding.dataInclusao.text = dateText
                        binding.nomeLoja.text = oferta.nomeLoja
                        binding.endereco.text = oferta.endereco
                        binding.precoAntigo.paintFlags =
                            binding.precoAntigo.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                        binding.precoAntigo.text = oferta.valorAntigo?.toString()
                        binding.precoNovo.text = oferta.valorNovo.toString()
                        binding.descricaoOferta.text = oferta.descricao
                        binding.nomeUsuario.text = usuario?.nome
                        Glide.with(binding.fotoUsuario)
                            .load(usuario?.urlImagePerfil)
                            .into(binding.fotoUsuario)

                        binding.botaoAdicionarFavoritos.setOnClickListener {
                            viewModel.saveOrRemoveFavorite(oferta)
                        }

                    }
                }
            }
        }
    }

    private fun configRecyclerViewImages() {
        recyclerView = binding.recyclerViewImagens
        recyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        listaImagemAdapter = ListaImagemAdapter(listOf())
        recyclerView.adapter = listaImagemAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
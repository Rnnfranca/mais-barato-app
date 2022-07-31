package com.example.maisbarato.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.maisbarato.R
import com.example.maisbarato.databinding.FragmentFavoritesBinding
import com.example.maisbarato.util.StateViewResult
import com.example.maisbarato.view.adapter.ListaOfertaAdapter
import com.example.maisbarato.viewmodel.FavoritesViewModel
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoritesViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var listaOfertasAdapter: ListaOfertaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configRecyclerView()

        viewModel.getFavorites()

        observers()
        clickListeners()

    }

    private fun clickListeners() {
        binding.swipeToRefresh.setOnRefreshListener {
            viewModel.getFavorites()
        }
    }

    private fun observers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stateView.collect {
                it?.also { stateViewResult ->

                    when (stateViewResult) {
                        is StateViewResult.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is StateViewResult.Success -> {
                            stateViewResult.result?.also { listaOfertas ->
                                binding.swipeToRefresh.isRefreshing = false
                                binding.progressBar.visibility = View.GONE
                                listaOfertasAdapter.atualizaListaOferta(listaOfertas)
                            }
                        }

                        is StateViewResult.Error -> {
                            binding.swipeToRefresh.isRefreshing = false
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.falha_carregar_favoritos),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun configRecyclerView() {
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )

        listaOfertasAdapter = ListaOfertaAdapter(listOf()) { oferta ->
            val action =
                FavoritesFragmentDirections.actionFavoritesFragmentToDetalhesOfertaFragment(
                    oferta
                )
            findNavController().navigate(action)
        }

        recyclerView.adapter = listaOfertasAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
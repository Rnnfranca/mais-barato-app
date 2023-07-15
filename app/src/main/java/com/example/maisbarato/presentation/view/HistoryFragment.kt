package com.example.maisbarato.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.maisbarato.databinding.FragmentHistoryBinding
import com.example.maisbarato.util.StateViewResult
import com.example.maisbarato.view.adapter.HistoricoOfertaAdapter
import com.example.maisbarato.viewmodel.HistoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HistoryViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var listaOfertasAdapter: HistoricoOfertaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        viewModel.getOfferHistory()
        observers()
    }

    private fun setupRecyclerView() {
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        listaOfertasAdapter = HistoricoOfertaAdapter(listOf(),
            { offer ->

            val action = HistoryFragmentDirections.actionHistoryFragmentToDetalhesOfertaFragment(
                offer
            )
            findNavController().navigate(action)
            },
            { offer ->
                viewModel.deleteOfferFromHistory(offer)
                viewModel.getOfferHistory()
            }
        )
        recyclerView.adapter = listaOfertasAdapter

    }

    private fun observers() {

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.oferta.collect { listaOfertas ->
                    listaOfertasAdapter.atualizaListaHistorico(listaOfertas)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateView.collect { stateView ->

                    when(stateView) {
                        is StateViewResult.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is StateViewResult.Success, is StateViewResult.Error -> {
                            binding.progressBar.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
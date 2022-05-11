package com.example.maisbarato.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.maisbarato.databinding.FragmentListaOfertasBinding
import com.example.maisbarato.view.adapter.ListaOfertaAdapter
import com.example.maisbarato.viewmodel.ListaOfertasViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListaOfertasFragment : Fragment() {

    private var _binding: FragmentListaOfertasBinding? = null
    private val binding get() = _binding!!
    private val listaOfertasViewModel: ListaOfertasViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var listaOfertasAdapter: ListaOfertaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentListaOfertasBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        listaOfertasAdapter = ListaOfertaAdapter(listOf()) { oferta ->
            val action = ListaOfertasFragmentDirections.actionListaOfertasFragmentToDetalhesOfertaFragment(
                oferta
            )
            findNavController().navigate(action)
        }
        recyclerView.adapter = listaOfertasAdapter

        listaOfertasViewModel.lerTodasOfertas()

        binding.fabAddOferta.setOnClickListener {
            val action = ListaOfertasFragmentDirections.actionListaOfertasFragmentToCrudOfertaFragment()
            binding.fabAddOferta.findNavController().navigate(action)
        }

        listaOfertasViewModel.oferta.observe(viewLifecycleOwner) { listaOfertas ->
            listaOfertasAdapter.atualizaListaOferta(listaOfertas)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
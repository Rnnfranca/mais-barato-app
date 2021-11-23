package com.example.maisbarato.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.maisbarato.databinding.OfertaItemBinding
import com.example.maisbarato.model.Oferta

class ListaOfertaAdapter(private var listaOferta: List<Oferta>) :
    RecyclerView.Adapter<ListaOfertaAdapter.OfertaViewHolder>() {

    inner class OfertaViewHolder(private val binding: OfertaItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(oferta: Oferta) {

            oferta.apply {

                Glide.with(binding.imagemOferta)
                    .load(oferta.listaUrlImagem.firstOrNull())
                    .into(binding.imagemOferta)

                binding.tituloPromocao.text = titulo
                binding.nomeLoja.text = nomeLoja
                binding.dataInclusao.text = ""
                binding.precoAntigo.text = valorAntigo.toString()
                binding.precoNovo.text = valorNovo.toString()

            }
        }

    }

    fun atualizaListaOferta(listaOferta: List<Oferta>) {
        this.listaOferta = listaOferta
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfertaViewHolder {
        val ofertaItemBinding = OfertaItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return OfertaViewHolder(ofertaItemBinding)
    }

    override fun onBindViewHolder(holder: OfertaViewHolder, position: Int) {
        holder.bind(listaOferta[position])
    }

    override fun getItemCount(): Int  = listaOferta.size

}
package com.example.maisbarato.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.maisbarato.databinding.ImagemItemBinding

class ListaImagemAdapter(private var listaUriImagens: List<String>) :
 RecyclerView.Adapter<ListaImagemAdapter.ImagemViewHolder>() {

     inner class ImagemViewHolder(private val binding: ImagemItemBinding) :
         RecyclerView.ViewHolder(binding.root) {

         fun bind(imagemUri: String, position: Int) {
             Glide.with(binding.imagemProduto)
                 .load(imagemUri)
                 .into(binding.imagemProduto)

            val currentImage = position+1
             binding.contadorImagens.text = "$currentImage/${listaUriImagens.size}"
        }

     }

    fun atualizaListaImagens(listaUriImagens: List<String>) {
        this.listaUriImagens = listaUriImagens
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagemViewHolder {
        val imagemItemBinding = ImagemItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ImagemViewHolder(imagemItemBinding)
    }

    override fun onBindViewHolder(holder: ImagemViewHolder, position: Int) {
        holder.bind(listaUriImagens[position], position)
    }

    override fun getItemCount(): Int {
        return listaUriImagens.size
    }
}
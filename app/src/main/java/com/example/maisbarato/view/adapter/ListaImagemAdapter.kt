package com.example.maisbarato.view.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.maisbarato.databinding.ImagemItemBinding

class ListaImagemAdapter(private var listaUriImagens: List<Uri>) :
 RecyclerView.Adapter<ListaImagemAdapter.ImagemViewHolder>() {

     inner class ImagemViewHolder(private val binding: ImagemItemBinding) :
         RecyclerView.ViewHolder(binding.root) {

         fun bind(imagemUri: Uri) {
             Glide.with(binding.imagemProduto)
                 .load(imagemUri)
                 .into(binding.imagemProduto)
        }

     }

    fun atualizaListaImagens(listaUriImagens: List<Uri>) {
        this.listaUriImagens = listaUriImagens
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagemViewHolder {
        val imagemItemBinding = ImagemItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ImagemViewHolder(imagemItemBinding)
    }

    override fun onBindViewHolder(holder: ImagemViewHolder, position: Int) {
        holder.bind(listaUriImagens[position])
    }

    override fun getItemCount(): Int {
        return listaUriImagens.size
    }
}
package com.example.maisbarato.presentation.view.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.maisbarato.R
import com.example.maisbarato.databinding.OfertaHistoricoItemBinding
import com.example.maisbarato.data.model.Oferta
import java.text.SimpleDateFormat
import java.util.*

class HistoricoOfertaAdapter(
    private var listaOferta: List<Oferta>,
    val clickListenerCard: (Oferta) -> Unit,
    val clickListenerDelete: (Oferta) -> Unit
) :
    RecyclerView.Adapter<HistoricoOfertaAdapter.OfertaHistoricoViewHolder>() {

    inner class OfertaHistoricoViewHolder(private val binding: OfertaHistoricoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(oferta: Oferta, clickListener: (Oferta) -> Unit, clickListenerDelete: (Oferta) -> Unit) {

            oferta.apply {

                Glide.with(binding.imagemOferta)
                    .load(listaUrlImagem.firstOrNull())
                    .into(binding.imagemOferta)

                val data = Date(dataInclusao)
                val formatDate = SimpleDateFormat("dd/MM/yyyy")
                val dateText = formatDate.format(data)

                val dataAcesso = Date(dataAcesso ?: 0)
                val dataAcessoText = formatDate.format(dataAcesso)

                binding.precoAntigo.paintFlags = binding.precoAntigo.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                binding.tituloPromocao.text = titulo
                binding.nomeLoja.text = nomeLoja
                binding.dataInclusao.text = dateText
                binding.precoAntigo.text = valorAntigo?.toString()
                binding.precoNovo.text = valorNovo.toString()
                binding.dataAcesso.text = binding.root.resources.getString(R.string.acessado_em, dataAcessoText)

                binding.root.setOnClickListener { clickListener(oferta) }
                binding.btnExcluir.setOnClickListener { clickListenerDelete(oferta) }
            }
        }
    }

    fun atualizaListaHistorico(listaOferta: List<Oferta>) {
        this.listaOferta = listaOferta
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfertaHistoricoViewHolder {
        val ofertaItemBinding = OfertaHistoricoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return OfertaHistoricoViewHolder(ofertaItemBinding)
    }

    override fun onBindViewHolder(holder: OfertaHistoricoViewHolder, position: Int) {
        holder.bind(listaOferta[position], clickListenerCard, clickListenerDelete)
    }

    override fun getItemCount(): Int = listaOferta.size

}
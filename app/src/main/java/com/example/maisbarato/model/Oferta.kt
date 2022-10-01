package com.example.maisbarato.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Oferta (
    var idRoom: Int = 0,
    var uid: String = "",
    var titulo: String = "",
    var listaUrlImagem: List<String> = listOf(),
    var dataInclusao: Long = 0,
    var dataAcesso: Long? = 0,
    var fotoLoja: String = "",
    var nomeLoja: String = "",
    var endereco: String = "",
    var valorAntigo: Double? = 0.0,
    var valorNovo: Double? = 0.0,
    var descricao: String = "",
    var userUid: String = ""
): Parcelable
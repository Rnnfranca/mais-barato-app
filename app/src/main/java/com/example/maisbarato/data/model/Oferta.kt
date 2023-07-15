package com.example.maisbarato.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "oferta")
data class Oferta (
    @PrimaryKey
    var idRoom: Int = 0,
    var id: String = "",
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
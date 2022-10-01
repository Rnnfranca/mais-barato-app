package com.example.maisbarato.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "offer")
data class OfferEntity(
    @PrimaryKey
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
)

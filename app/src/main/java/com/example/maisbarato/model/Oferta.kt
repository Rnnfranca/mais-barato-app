package com.example.maisbarato.model

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "oferta")
data class Oferta (
    var titulo: String = "",
    var imagem: Bitmap? = null,
    var dataInclusao: String = "",
    var fotoLoja: Bitmap? = null,
    var nomeLoja: String = "",
    var temperatura: Int? = null,
    var valorAntigo: Double = 0.0,
    var valorNovo: Double = 0.0,
    var descricao: String = ""
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
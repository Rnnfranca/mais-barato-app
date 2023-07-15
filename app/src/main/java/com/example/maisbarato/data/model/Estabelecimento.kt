package com.example.maisbarato.data.model

import android.graphics.Bitmap

data class Estabelecimento (
    var id: Long = 0,
    var nome: String = "",
    var imagem: Bitmap? = null,
    var endereco: Endereco = Endereco(),
    var site: String = ""
)
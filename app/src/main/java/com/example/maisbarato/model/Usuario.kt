package com.example.maisbarato.model

data class Usuario(
    var nome: String? = null,
    var sobreNome: String? = null,
    var telefone: Long? = null,
    var email: String? = null,
    var endereco: List<Endereco>? = null
)

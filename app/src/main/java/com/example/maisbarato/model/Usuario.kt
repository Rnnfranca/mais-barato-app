package com.example.maisbarato.model

data class Usuario(
    var nome: String? = null,
    var sobreNome: String? = null,
    var urlImagePerfil: String? = null,
    var telefone: String? = null,
    var email: String? = null,
    var endereco: List<Endereco>? = null
)

package com.example.maisbarato.model

data class Endereco (
    var cep: Int = 0,
    var logradouro: String = "",
    var numero: Int = 0,
    var bairro: String = "",
    var cidade: String = ""
)
package com.example.maisbarato.util

import java.util.regex.Pattern

class ValidateUtil {

    companion object {
        fun validaEmail(email: String): Boolean {
            return !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun validaNome(nome: String): Boolean {
            return nome.length < 3 || !nome.contains(" ")
        }

        fun validaSenha(senha: String): Boolean {

            val pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$")
            val matcher = pattern.matcher(senha)

            return !matcher.matches()
        }
    }
}
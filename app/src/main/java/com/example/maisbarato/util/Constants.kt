package com.example.maisbarato.util

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.maisbarato.R

const val OFERTA_COLLECTION = "oferta"
val PREFERENCE_EMAIL = stringPreferencesKey("email_data_key")
val PREFERENCE_SENHA = stringPreferencesKey("senha_data_key")
val PREFERENCE_SWITCH_STATUS = booleanPreferencesKey("switch_status_key")
val PREFERENCE_USER_UID = stringPreferencesKey("user_uid")

val telasComIconeMenuHamburguer = setOf(
    R.id.listaOfertasFragment
)

val telasSemToolbar = setOf(
    R.id.loginFragment,
    R.id.cadastroFragment
)

val telasSemMenuDrawer = setOf(
    R.id.loginFragment,
    R.id.cadastroFragment,
    R.id.detalhesOfertaFragment
)
package com.example.maisbarato.util

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

const val OFERTA_COLLECTION = "oferta"
val PREFERENCE_EMAIL_KEY = stringPreferencesKey("email_data_key")
val PREFERENCE_SENHA_KEY = stringPreferencesKey("senha_data_key")
val PREFERENCE_SWITCH_STATUS_KEY = booleanPreferencesKey("switch_status_key")
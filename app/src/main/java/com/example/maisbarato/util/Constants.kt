package com.example.maisbarato.util

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

const val OFERTA_COLLECTION = "oferta"
val PREFERENCE_EMAIL = stringPreferencesKey("email_data_key")
val PREFERENCE_SENHA = stringPreferencesKey("senha_data_key")
val PREFERENCE_SWITCH_STATUS = booleanPreferencesKey("switch_status_key")
val PREFERENCE_USER_UID = stringPreferencesKey("user_uid")
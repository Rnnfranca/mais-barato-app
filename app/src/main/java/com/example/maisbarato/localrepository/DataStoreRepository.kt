package com.example.maisbarato.localrepository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.maisbarato.model.LoginUsuario
import com.example.maisbarato.util.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "mais_barato_app_preferences")


class DataStoreRepository(private val context: Context) {

    fun getLoginUsuario(): Flow<RepositoryResult<LoginUsuario>> =
        context.dataStore.data.catch {
            RepositoryResult.Error(
                it.message.toString()
            )
        }.map { preferences ->
            RepositoryResult.Success(
                LoginUsuario(
                    preferences[PREFERENCE_EMAIL],
                    preferences[PREFERENCE_SENHA]
                )
            )
        }

    suspend fun salvaLoginUsuario(email: String, senha: String) {
        context.dataStore.edit { preferences ->
            preferences[PREFERENCE_EMAIL] = email
            preferences[PREFERENCE_SENHA] = senha
        }
    }

    suspend fun limpaLoginSalvo() {
        context.dataStore.edit { preferences ->
            preferences[PREFERENCE_EMAIL] = ""
            preferences[PREFERENCE_SENHA] = ""
        }
    }

    suspend fun salvaSwitchLoginStatus(status: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PREFERENCE_SWITCH_STATUS] = status
        }
    }

    fun getSwitchLoginStatus(): Flow<RepositoryResult<Boolean>> =
        context.dataStore.data.catch {
            RepositoryResult.Error(
                it.message.toString()
            )
        }.map { preferences ->
            RepositoryResult.Success(
                preferences[PREFERENCE_SWITCH_STATUS] ?: false
            )
        }

    suspend fun salvaUIDUsuario(uid: String) {
        context.dataStore.edit { preferences ->
            preferences[PREFERENCE_USER_UID] = uid
        }
    }

    fun getUIDUsuario(): Flow<RepositoryResult<String>> =
        context.dataStore.data.catch {
            RepositoryResult.Error(
                it.message.toString()
            )
        }.map { preferences ->
            RepositoryResult.Success(
                preferences[PREFERENCE_USER_UID] ?: ""
            )
        }

    suspend fun getUIDUsuario2(): String =
        context.dataStore.data.first()[PREFERENCE_USER_UID] ?: ""

    suspend fun salvaURLImagemUsuario(urlImagem: String) {
        context.dataStore.edit { preferences ->
            preferences[PREFERENCE_URL_IMAGEM_USUARIO] = urlImagem
        }
    }

    fun getURLImagemUsuario(): Flow<RepositoryResult<String>> =
        context.dataStore.data.catch {
            RepositoryResult.Error(
                it.message.toString()
            )
        }.map { preferences ->
            RepositoryResult.Success(
                preferences[PREFERENCE_URL_IMAGEM_USUARIO] ?: ""
            )
        }
}
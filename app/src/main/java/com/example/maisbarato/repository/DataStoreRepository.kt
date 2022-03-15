package com.example.maisbarato.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.maisbarato.model.LoginUsuario
import com.example.maisbarato.util.PREFERENCE_EMAIL_KEY
import com.example.maisbarato.util.PREFERENCE_SENHA_KEY
import com.example.maisbarato.util.PREFERENCE_SWITCH_STATUS_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "mais_barato_app_preferences")


class DataStoreRepository(private val context: Context) {

    fun getLoginUsuario(): Flow<RepositoryResult<LoginUsuario>> =
        context.dataStore.data.catch {
            RepositoryResult.Error(
                it.message.toString()
            )
        }.map { settings ->
            RepositoryResult.Success(
                LoginUsuario(
                    settings[PREFERENCE_EMAIL_KEY],
                    settings[PREFERENCE_SENHA_KEY]
                )
            )
        }

    suspend fun salvaLoginUsuario(email: String, senha: String) {
        context.dataStore.edit { settings ->
            settings[PREFERENCE_EMAIL_KEY] = email
            settings[PREFERENCE_SENHA_KEY] = senha
        }
    }

    suspend fun limpaLoginSalvo() {
        context.dataStore.edit { settings ->
            settings[PREFERENCE_EMAIL_KEY] = ""
            settings[PREFERENCE_SENHA_KEY] = ""
        }
    }

    suspend fun salvaSwitchLoginStatus(status: Boolean) {
        context.dataStore.edit { settings ->
            settings[PREFERENCE_SWITCH_STATUS_KEY] = status
        }
    }

    fun getSwitchLoginStatus(): Flow<RepositoryResult<Boolean>> =
        context.dataStore.data.catch {
            RepositoryResult.Error(
                it.message.toString()
            )
        }.map { settings ->
            RepositoryResult.Success(
                settings[PREFERENCE_SWITCH_STATUS_KEY] ?: false
            )
        }
}
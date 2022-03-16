package com.example.maisbarato.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.maisbarato.repository.DataStoreRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CadastroViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = CadastroViewModel::class.java.name

    private val dataStore = DataStoreRepository(application)

    fun salvaUIDUsuario(uid: String, dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        viewModelScope.launch(dispatcher) {
            try {
                dataStore.salvaUIDUsuario(uid)
            } catch (e: Exception) {
                Log.e(TAG, e.message ?: "Erro ao salvar UID do usuário")
            }
        }
    }

}
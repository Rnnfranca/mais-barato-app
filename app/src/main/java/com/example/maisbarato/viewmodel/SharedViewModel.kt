package com.example.maisbarato.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.maisbarato.model.Usuario
import com.example.maisbarato.repository.auth.AuthenticationRepository
import com.example.maisbarato.repository.local.DataStoreRepository
import com.example.maisbarato.repository.local.RepositoryResult
import com.example.maisbarato.util.COLLECTION_USUARIO
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = SharedViewModel::class.java.name
    private var authRepository = AuthenticationRepository()

    val currentUser get() = authRepository.currentUser

    private val remoteDatabase = Firebase.firestore
    private val dataStore = DataStoreRepository(application)

    private val _dadosUsuario = MutableStateFlow<Usuario?>(null)
    val dadosUsuario = _dadosUsuario.asStateFlow()

    private val _urlImagemUsuario = MutableStateFlow<String?>(null)
    val urlImagemUsuario = _urlImagemUsuario.asStateFlow()

    fun logout() {
        authRepository.signOut()
    }

    fun carregaUID(dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        viewModelScope.launch(dispatcher) {
            dataStore.getUIDUsuario().collect { repositoryResult ->

                when (repositoryResult) {
                    is RepositoryResult.Success -> {
                        val uidUsuario = repositoryResult.result

                        if (uidUsuario.isNotEmpty()) {
                            carregaDadosUsuario(uidUsuario)
                        } else {
                            authRepository.currentUser?.also { firebaseUser ->
                                carregaDadosUsuario(firebaseUser.uid)
                            }
                        }
                    }
                }

            }
        }
    }

    fun carregaFotoUsuario(dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        viewModelScope.launch(dispatcher) {
            dataStore.getURLImagemUsuario().collect { repositoryResult ->

                when (repositoryResult) {
                    is RepositoryResult.Success -> {
                        _urlImagemUsuario.emit(repositoryResult.result)
                    }

                    is RepositoryResult.Error -> {
                        Log.e(TAG, repositoryResult.error)
                    }
                }

            }
        }
    }

    private fun carregaDadosUsuario(uid: String, dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        viewModelScope.launch(dispatcher) {

            val usuarioDoc = remoteDatabase.collection(COLLECTION_USUARIO)
                .document(uid)
                .get()

            usuarioDoc.addOnSuccessListener { documento ->
                documento?.also {

                    it.data?.also { usuario ->

                        viewModelScope.launch(dispatcher) {
                            _dadosUsuario.emit(
                                Usuario(
                                    nome = usuario["nome"].toString(),
                                    telefone = usuario["telefone"].toString(),
                                    email = usuario["email"].toString(),
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
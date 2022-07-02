package com.example.maisbarato.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.maisbarato.localrepository.DataStoreRepository
import com.example.maisbarato.localrepository.RepositoryResult
import com.example.maisbarato.model.Usuario
import com.example.maisbarato.util.COLLECTION_USUARIO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = SharedViewModel::class.java.name

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    val currentUser get() = auth.currentUser

    private val remoteDatabase = Firebase.firestore
    private val dataStore = DataStoreRepository(application)

    private val _dadosUsuario = MutableSharedFlow<Usuario>(replay = 2, onBufferOverflow = BufferOverflow.DROP_LATEST)
    val dadosUsuario: SharedFlow<Usuario> = _dadosUsuario

    fun logout() {
        auth.signOut()
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
                            auth.currentUser?.also { firebaseUser ->
                                carregaDadosUsuario(firebaseUser.uid)
                            }
                        }
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
                                    telefone = usuario["telefone"].toString().toLongOrNull(),
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
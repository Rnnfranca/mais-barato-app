package com.example.maisbarato.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.maisbarato.localrepository.DataStoreRepository
import com.example.maisbarato.model.Usuario
import com.example.maisbarato.util.COLLECTION_USUARIO
import com.example.maisbarato.util.StateViewResult
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CadastroViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = CadastroViewModel::class.java.name

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val remoteDataBase = Firebase.firestore

    private val dataStore = DataStoreRepository(application)

    private val _stateView: MutableStateFlow<StateViewResult<Any>> = MutableStateFlow(StateViewResult.Initial)
    val stateView = _stateView.asStateFlow()

    private val _emailAutenticationState: MutableStateFlow<StateViewResult<Any>> = MutableStateFlow(StateViewResult.Initial)
    val emailAutenticationState = _emailAutenticationState.asStateFlow()

    fun criarUsuario(nome: String, email: String, senha: String, dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        _stateView.value = StateViewResult.Loading

        viewModelScope.launch(dispatcher) {
            auth.createUserWithEmailAndPassword(
                email,
                senha
            ).addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    Log.d(TAG, "createUserWithEmail:success")

                    task.result?.user?.also { firebaseUser ->
                        salvaUIDUsuario(firebaseUser.uid)

                        val usuario = Usuario(
                            nome = nome,
                            email = email
                        )

                        remoteDataBase.collection(COLLECTION_USUARIO)
                            .document(firebaseUser.uid)
                            .set(usuario)
                    }

                    _stateView.value = StateViewResult.Success()
                    enviaEmailVerificacao(task)

                } else {

                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    _stateView.value = StateViewResult.Error(task.exception.toString())
                }
            }
        }
    }

    private fun salvaUIDUsuario(uid: String, dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        viewModelScope.launch(dispatcher) {
            try {
                dataStore.salvaUIDUsuario(uid)
            } catch (e: Exception) {
                Log.e(TAG, e.message ?: "Erro ao salvar UID do usuário")
            }
        }
    }

    private fun enviaEmailVerificacao(task: Task<AuthResult>, dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        val user = task.result?.user

        _emailAutenticationState.value = StateViewResult.Loading
        viewModelScope.launch(dispatcher) {
            user?.sendEmailVerification()
                ?.addOnCompleteListener { taskEmailVerification ->

                    if (taskEmailVerification.isSuccessful) {

                        _emailAutenticationState.value = StateViewResult.Success(result = user.email)
                    } else {

                        Log.e(TAG, "sendEmailVerification", taskEmailVerification.exception)
                        _emailAutenticationState.value = StateViewResult.Error(taskEmailVerification.exception.toString())
                    }
                }
        }


    }

}
package com.example.maisbarato.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.viewModelScope
import com.example.maisbarato.model.LoginUsuario
import com.example.maisbarato.repository.auth.AuthenticationRepository
import com.example.maisbarato.repository.local.DataStoreRepository
import com.example.maisbarato.repository.local.RepositoryResult
import com.example.maisbarato.util.StateViewResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    application: Application,
    private val authRepository: AuthenticationRepository
) : AndroidViewModel(application), LifecycleObserver {

    private val TAG = LoginViewModel::class.java.name

    private val dataStore = DataStoreRepository(application)

    private val _stateView = MutableStateFlow<StateViewResult<Any>>(StateViewResult.Initial)
    val stateView =  _stateView.asStateFlow()

    private val _loginUsuario = MutableStateFlow<StateViewResult<LoginUsuario>>(StateViewResult.Initial)
    val loginUsuario = _loginUsuario.asStateFlow()

    private val _switchStatus = MutableStateFlow<StateViewResult<Boolean>>(StateViewResult.Initial)
    val switchStatus = _switchStatus.asStateFlow()

    fun login(email: String, senha: String) {
        _stateView.value = StateViewResult.Loading
        viewModelScope.launch {
            authRepository.login(email, senha)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        task.result?.user?.also { firebaseUser ->
                            salvaUIDUsuario(firebaseUser.uid)
                        }

                        _stateView.value = StateViewResult.Success(result = task)
                    } else {
                        _stateView.value = StateViewResult.Error(errorMsg = task.exception.toString())
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

    fun salvaDadosLogin(
        email: String,
        senha: String,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ) {
        viewModelScope.launch(dispatcher) {
            try {
                dataStore.salvaLoginUsuario(email, senha)
            } catch (e: Exception) {
                Log.e(TAG, e.message ?: "Erro ao salvar login usuário")
            }
        }
    }

    fun getDadosLogin(dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        viewModelScope.launch(dispatcher) {
            dataStore.getLoginUsuario().collect { repositoryResult ->
                when(repositoryResult) {
                    is RepositoryResult.Success -> {
                        if (!repositoryResult.result.email.isNullOrBlank()) {
                            _loginUsuario.value = StateViewResult.Success(repositoryResult.result)
                        }
                    }

                    is RepositoryResult.Error -> {
                        Log.e(TAG, repositoryResult.error)

                        _loginUsuario.value = StateViewResult.Error("Não foi possível recuperar os dados de login")
                    }
                }
            }
        }
    }

    fun limpaLoginSalvo(dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        viewModelScope.launch(dispatcher) {
            try {
                dataStore.limpaLoginSalvo()
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }
    }

    fun salvaSwitchLoginStatus(status: Boolean, dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        viewModelScope.launch(dispatcher) {
            try {
                dataStore.salvaSwitchLoginStatus(status)
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }
    }

    fun getSwitchLoginStatus(dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        viewModelScope.launch(dispatcher) {
            dataStore.getSwitchLoginStatus().collect { repositoryResult ->
                when (repositoryResult) {
                    is RepositoryResult.Success -> {
                        _switchStatus.value = StateViewResult.Success(repositoryResult.result)
                    }

                    is RepositoryResult.Error -> {
                        Log.e(TAG, repositoryResult.error)

                        _switchStatus.value = StateViewResult.Error("Não foi possível recuperar o status do switch")
                    }
                }
            }
        }
    }
}
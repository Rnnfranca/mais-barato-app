package com.example.maisbarato.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.maisbarato.model.LoginUsuario
import com.example.maisbarato.repository.DataStoreRepository
import com.example.maisbarato.repository.RepositoryResult
import com.example.maisbarato.util.SingleLiveEvent
import com.example.maisbarato.util.StateViewResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application), LifecycleObserver {

    private val TAG = "LoginViewModel"

    private val dataStore = DataStoreRepository(application)
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()


    private val _stateView = MutableLiveData<SingleLiveEvent<StateViewResult<Any>>>()
    val stateView: LiveData<SingleLiveEvent<StateViewResult<Any>>> get() =  _stateView

    private val _loginUsuario = MutableLiveData<SingleLiveEvent<StateViewResult<LoginUsuario>>>()
    val loginUsuario: LiveData<SingleLiveEvent<StateViewResult<LoginUsuario>>> = _loginUsuario

    private val _switchStatus = MutableLiveData<SingleLiveEvent<StateViewResult<Boolean>>>()
    val switchStatus: LiveData<SingleLiveEvent<StateViewResult<Boolean>>> = _switchStatus

    fun login(email: String, senha: String,) {
        _stateView.postValue(SingleLiveEvent(StateViewResult.Loading))
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(
                email,
                senha
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    _stateView.postValue(
                        SingleLiveEvent(
                            StateViewResult.Success(result = task)
                        )
                    )
                } else {
                    _stateView.postValue(
                        SingleLiveEvent(
                            StateViewResult.Error(
                                errorMsg = task.exception.toString()
                            )
                        )
                    )
                }
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
                Log.d(TAG, e.message ?: "Erro ao salvar login usuário")
            }
        }
    }

    fun getLogin(dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        viewModelScope.launch(dispatcher) {
            dataStore.getLoginUsuario().collect { repositoryResult ->
                when(repositoryResult) {
                    is RepositoryResult.Success -> {
                        if (!repositoryResult.result.email.isNullOrBlank()) {
                            _loginUsuario.postValue(
                                SingleLiveEvent(
                                    StateViewResult.Success(
                                        repositoryResult.result
                                    )
                                )
                            )
                        }
                    }

                    is RepositoryResult.Error -> {
                        Log.e(TAG, repositoryResult.error)

                        _loginUsuario.postValue(
                            SingleLiveEvent(
                                StateViewResult.Error(
                                    "Não foi possível recuperar os dados de login"
                                )
                            )
                        )
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
                        _switchStatus.postValue(
                            SingleLiveEvent(
                                StateViewResult.Success(
                                    repositoryResult.result
                                )
                            )
                        )
                    }

                    is RepositoryResult.Error -> {
                        Log.e(TAG, repositoryResult.error)

                        _switchStatus.postValue(
                            SingleLiveEvent(
                                StateViewResult.Error(
                                    "Não foi possível recuperar o status do switch"
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}
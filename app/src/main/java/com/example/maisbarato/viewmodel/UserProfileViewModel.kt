package com.example.maisbarato.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.maisbarato.model.Usuario
import com.example.maisbarato.repository.auth.AuthenticationRepository
import com.example.maisbarato.repository.firebase.FirebaseRepository
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
class UserProfileViewModel @Inject constructor(
    application: Application,
    authRepository: AuthenticationRepository
) : AndroidViewModel(application) {
    private val dataStore = DataStoreRepository(application)
    private val firebaseRepository = FirebaseRepository()

    private val _imageUserURL = MutableStateFlow<String?>(null)
    val imageUserUrl = _imageUserURL.asStateFlow()

    private val _userInfo = MutableStateFlow<Usuario?>(null)
    val userInfo = _userInfo.asStateFlow()

    private val _stateView: MutableStateFlow<StateViewResult<Any>?> = MutableStateFlow(null)
    val stateView = _stateView.asStateFlow()

    var userUid: String = authRepository.currentUser?.uid ?: ""

    fun setImageUri(url: String) {
        _imageUserURL.value = url
    }

    fun getImageUrl(dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        viewModelScope.launch(dispatcher) {
            dataStore.getURLImagemUsuario().collect { repositoryResult ->

                when (repositoryResult) {
                    is RepositoryResult.Success -> {
                        _imageUserURL.emit(repositoryResult.result)
                    }
                }
            }
        }
    }

    fun getInfoUser(dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        viewModelScope.launch(dispatcher) {
            firebaseRepository.getUserInfo(userUid)?.also { user ->
                _userInfo.emit(user)
            }
        }
    }

    fun updateInfo(fullName: String, email: String, dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        viewModelScope.launch(dispatcher) {
            _stateView.emit(StateViewResult.Loading)

            firebaseRepository.updateUserInfo(userUid, fullName, email, _imageUserURL.value) { isUpdate, urlImage ->
                viewModelScope.launch(dispatcher) {
                    dataStore.salvaURLImagemUsuario(urlImage)
                    if (isUpdate) {
                        _stateView.emit(StateViewResult.Success())
                    } else {
                        _stateView.emit(StateViewResult.Error())
                    }
                }
            }

        }
    }
}
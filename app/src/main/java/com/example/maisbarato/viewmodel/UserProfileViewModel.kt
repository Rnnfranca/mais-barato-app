package com.example.maisbarato.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.maisbarato.localrepository.DataStoreRepository
import com.example.maisbarato.localrepository.RepositoryResult
import com.example.maisbarato.repository.firebase.FirebaseRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

class UserProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val dataStore = DataStoreRepository(application)
    private val firebaseRepository = FirebaseRepository(application)

    private val _imageUserURL = MutableStateFlow<String?>(null)
    val imageUserUrl = _imageUserURL.asStateFlow()

    fun salvaURLImagem(uri: Uri, dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        viewModelScope.launch(dispatcher) {
            dataStore.getUIDUsuario().take(1).collect { repositoryResult ->

                when (repositoryResult) {
                    is RepositoryResult.Success -> {
                        firebaseRepository.salvaImagemUsuario(uri, repositoryResult.result) { urlImagem ->
                            viewModelScope.launch(dispatcher) {
                                dataStore.salvaURLImagemUsuario(urlImagem)
                            }
                        }
                    }
                }
            }
        }
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

    fun updateInfo(fullName: String, email: String, phone: String, imageUrl: String, dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        viewModelScope.launch(dispatcher) {
            firebaseRepository
        }
    }
}
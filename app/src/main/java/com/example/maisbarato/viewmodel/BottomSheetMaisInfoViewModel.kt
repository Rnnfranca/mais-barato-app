package com.example.maisbarato.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.maisbarato.localrepository.DataStoreRepository
import com.example.maisbarato.localrepository.RepositoryResult
import com.example.maisbarato.remoterepository.FirebaseRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BottomSheetMaisInfoViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStore = DataStoreRepository(application)
    private val firebaseRepository = FirebaseRepository(application)

    fun salvaURLImagem(uri: Uri, dispatcher: CoroutineDispatcher = Dispatchers.IO) {

        viewModelScope.launch(dispatcher) {
            dataStore.getUIDUsuario().collect { repositoryResult ->

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
}
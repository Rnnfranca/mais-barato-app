package com.example.maisbarato.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.maisbarato.localrepository.RepositoryResult
import com.example.maisbarato.model.Oferta
import com.example.maisbarato.repository.firebase.FirebaseRepository
import com.example.maisbarato.util.StateViewResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CrudOfertaViewModel @Inject constructor(application: Application) :
    AndroidViewModel(application) {

    val firebaseRepository = FirebaseRepository(application)

    private val _ofertaStateView = MutableStateFlow<StateViewResult<String>>(StateViewResult.Initial)
    val ofertaStateView = _ofertaStateView.asStateFlow()

    fun adicionaOferta(oferta: Oferta, dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        _ofertaStateView.value = StateViewResult.Loading
        viewModelScope.launch(dispatcher) {
            val response = firebaseRepository.salvaOferta(oferta)

            when (response) {
                is RepositoryResult.Success -> {
                    _ofertaStateView.value = StateViewResult.Success(response.result)
                }
                is RepositoryResult.Error -> {
                    _ofertaStateView.value = StateViewResult.Error(response.error)
                }
            }
        }
    }
}
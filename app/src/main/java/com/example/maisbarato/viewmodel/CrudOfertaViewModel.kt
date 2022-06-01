package com.example.maisbarato.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maisbarato.localrepository.OfertaRepository
import com.example.maisbarato.localrepository.RepositoryResult
import com.example.maisbarato.model.Oferta
import com.example.maisbarato.remoterepository.FirebaseRepository
import com.example.maisbarato.util.SingleLiveEvent
import com.example.maisbarato.util.StateViewResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CrudOfertaViewModel @Inject constructor(val ofertaRepository: OfertaRepository) :
    ViewModel() {

    val firebaseRepository = FirebaseRepository()

    private val _ofertaStateView = MutableLiveData<SingleLiveEvent<StateViewResult<String>>>()
    val ofertaStateView: LiveData<SingleLiveEvent<StateViewResult<String>>> get() = _ofertaStateView

    fun adicionaOferta(oferta: Oferta) {
        _ofertaStateView.postValue(SingleLiveEvent(StateViewResult.Loading))
        viewModelScope.launch(Dispatchers.IO) {
            val response = firebaseRepository.salvaOferta(oferta)

            when (response) {
                is RepositoryResult.Success -> {
                    _ofertaStateView.postValue(
                        SingleLiveEvent(
                            StateViewResult.Success(
                                response.result
                            )
                        )
                    )
                }
                is RepositoryResult.Error -> {
                    _ofertaStateView.postValue(
                        SingleLiveEvent(
                            StateViewResult.Error(
                                response.error
                            )
                        )
                    )
                }
            }
        }
    }
}
package com.example.maisbarato.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maisbarato.model.Oferta
import com.example.maisbarato.repository.auth.AuthenticationRepository
import com.example.maisbarato.repository.local.OfertaRepository
import com.example.maisbarato.util.StateViewResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListaOfertasViewModel @Inject constructor(private val ofertaRepository: OfertaRepository) :
    ViewModel() {

    private var _stateView: MutableStateFlow<StateViewResult<Any>?> = MutableStateFlow(null)
    val stateView get() = _stateView.asStateFlow()

    private var _oferta = MutableLiveData<List<Oferta>>()
    val oferta: LiveData<List<Oferta>> get() = _oferta

    private var userUid = AuthenticationRepository.currentUser?.uid ?: ""

    fun lerTodasOfertas(dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        _stateView.value = StateViewResult.Loading
        viewModelScope.launch(dispatcher) {
            val listOferta = ofertaRepository.lerTodasOfertas()

            if (listOferta.isNotEmpty()) {
                _oferta.postValue(listOferta)
                _stateView.value = StateViewResult.Success()
            } else {
                _stateView.value = StateViewResult.Error("Falha ao carregar lista de ofertas")
            }
        }
    }

    fun addOfferToHistory(offer: Oferta, dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        viewModelScope.launch(dispatcher) {
            ofertaRepository.addOfferToHistory(userUid, offer)
        }
    }

}
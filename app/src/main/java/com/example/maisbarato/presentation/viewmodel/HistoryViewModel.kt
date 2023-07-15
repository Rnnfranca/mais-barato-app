package com.example.maisbarato.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maisbarato.data.model.Oferta
import com.example.maisbarato.data.repository.auth.AuthenticationRepository
import com.example.maisbarato.data.repository.local.OfertaRepository
import com.example.maisbarato.util.StateViewResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val ofertaRepository: OfertaRepository,
    authRepository: AuthenticationRepository
) : ViewModel() {

    private var _stateView: MutableStateFlow<StateViewResult<Any>?> = MutableStateFlow(null)
    val stateView get() = _stateView.asStateFlow()

    private var _oferta: MutableStateFlow<List<Oferta>> = MutableStateFlow(listOf())
    val oferta get() = _oferta.asStateFlow()

    private var userUid = authRepository.currentUser?.uid ?: ""

    fun getOfferHistory(dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        _stateView.value = StateViewResult.Loading

        viewModelScope.launch(dispatcher) {
            val offerHistory = ofertaRepository.getOfferHistory(userUid)

            if (offerHistory.isNotEmpty()) {

                _oferta.value = offerHistory.sortedByDescending { it.dataAcesso }
                _stateView.value = StateViewResult.Success()
            } else {
                _oferta.value = emptyList()
                _stateView.value = StateViewResult.Error()
            }
        }
    }

    fun deleteOfferFromHistory(offer: Oferta, dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        _stateView.value = StateViewResult.Loading

        viewModelScope.launch(dispatcher) {
            try {
                ofertaRepository.deleteOfferFromHistory(userUid, offer)
                _stateView.value = StateViewResult.Success()
            } catch (e: Exception) {
                _stateView.value = StateViewResult.Error()
            }
        }
    }

}
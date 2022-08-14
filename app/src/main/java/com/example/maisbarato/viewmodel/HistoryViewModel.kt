package com.example.maisbarato.viewmodel

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
class HistoryViewModel @Inject constructor(private val ofertaRepository: OfertaRepository) : ViewModel() {

    private var _stateView: MutableStateFlow<StateViewResult<Any>?> = MutableStateFlow(null)
    val stateView get() = _stateView.asStateFlow()

    private var _oferta: MutableStateFlow<List<Oferta>> = MutableStateFlow(listOf())
    val oferta get() = _oferta.asStateFlow()

    private var userUid = AuthenticationRepository.currentUser?.uid ?: ""

    fun getOfferHistory(dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        _stateView.value = StateViewResult.Loading

        viewModelScope.launch(dispatcher) {
            val offerHistory = ofertaRepository.getOfferHistory(userUid)

            if (offerHistory.isNotEmpty()) {

                _oferta.value = offerHistory.sortedByDescending { it.dataAcesso }
                _stateView.value = StateViewResult.Success()
            } else {
                _stateView.value = StateViewResult.Error()
            }
        }
    }

}
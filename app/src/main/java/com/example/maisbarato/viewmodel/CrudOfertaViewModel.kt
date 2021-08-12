package com.example.maisbarato.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maisbarato.model.Oferta
import com.example.maisbarato.repository.OfertaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CrudOfertaViewModel @Inject constructor(val ofertaRepository: OfertaRepository) :
    ViewModel() {

    fun adicionaOferta(oferta: Oferta) {
        viewModelScope.launch(Dispatchers.IO) {
            ofertaRepository.adicionaOferta(oferta)
        }
    }
}
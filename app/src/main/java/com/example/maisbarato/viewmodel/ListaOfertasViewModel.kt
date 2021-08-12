package com.example.maisbarato.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maisbarato.model.Oferta
import com.example.maisbarato.repository.OfertaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListaOfertasViewModel @Inject constructor(val ofertaRepository: OfertaRepository) :
    ViewModel() {

    private var _oferta = MutableLiveData<List<Oferta>>()
    val oferta: LiveData<List<Oferta>> get() = _oferta

    /*init {
        val ofertaDAO = MaisBaratoDatabase.getDatabase(application).ofertaDAO()
        repository = OfertaRepository(ofertaDAO)
    }*/

    fun lerTodasOfertas() {
        viewModelScope.launch(Dispatchers.IO) {
            ofertaRepository.lerTodasOfertas().let { listaOferta ->
                _oferta.postValue(listaOferta)
            }
        }
    }

}
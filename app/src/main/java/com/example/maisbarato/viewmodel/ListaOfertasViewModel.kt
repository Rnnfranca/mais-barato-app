package com.example.maisbarato.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.maisbarato.database.MaisBaratoDatabase
import com.example.maisbarato.model.Oferta
import com.example.maisbarato.repository.OfertaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListaOfertasViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: OfertaRepository

    private var _oferta = MutableLiveData<List<Oferta>>()
    val oferta: LiveData<List<Oferta>> get() = _oferta

    init {
        val ofertaDAO = MaisBaratoDatabase.getDatabase(application).ofertaDAO()
        repository = OfertaRepository(ofertaDAO)
    }

    fun lerTodasOfertas(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.lerTodasOfertas().let { listaOferta ->
                _oferta.postValue(listaOferta)
            }
        }
    }

}
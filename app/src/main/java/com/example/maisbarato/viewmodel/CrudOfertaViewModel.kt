package com.example.maisbarato.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.maisbarato.database.MaisBaratoDatabase
import com.example.maisbarato.model.Oferta
import com.example.maisbarato.repository.OfertaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CrudOfertaViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: OfertaRepository

    init {
        val ofertaDAO = MaisBaratoDatabase.getDatabase(application).ofertaDAO()
        repository = OfertaRepository(ofertaDAO)
    }

    fun adicionaOferta(oferta: Oferta) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.adicionaOferta(oferta)
        }
    }
}
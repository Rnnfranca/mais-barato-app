package com.example.maisbarato.repository

import com.example.maisbarato.database.OfertaDAO
import com.example.maisbarato.model.Oferta

class OfertaRepository(private val ofertaDAO: OfertaDAO) {

    suspend fun lerTodasOfertas(): List<Oferta> {
        return ofertaDAO.lerTodasOfertas()
    }

    suspend fun adicionaOferta(oferta: Oferta) {
        return ofertaDAO.adicionaOferta(oferta)
    }
}
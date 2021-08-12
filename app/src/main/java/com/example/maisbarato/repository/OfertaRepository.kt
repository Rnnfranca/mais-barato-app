package com.example.maisbarato.repository

import com.example.maisbarato.database.OfertaDAO
import com.example.maisbarato.model.Oferta
import javax.inject.Inject

class OfertaRepository @Inject constructor(val ofertaDAO: OfertaDAO) {

    suspend fun lerTodasOfertas(): List<Oferta> = ofertaDAO.lerTodasOfertas()

    suspend fun adicionaOferta(oferta: Oferta) = ofertaDAO.adicionaOferta(oferta)

}
package com.example.maisbarato.repository

import com.example.maisbarato.model.Oferta
import com.example.maisbarato.repository.local.RepositoryResult

class OfferRepositoryFake : OfferDataSource {

    override suspend fun getAllOffers(): RepositoryResult<List<Oferta>> {
        return RepositoryResult.Success(
            listOf(
                Oferta(titulo = "Oferta 1", descricao = "Teste", valorAntigo = 199.00, valorNovo = 99.00),
                Oferta(titulo = "Oferta 2", descricao = "Teste", valorAntigo = 820.00, valorNovo = 530.00),
                Oferta(titulo = "Oferta 3", descricao = "Teste", valorAntigo = 100.00, valorNovo = 50.00)
            )
        )
    }

    override suspend fun addOfferToHistory(userUid: String, offer: Oferta) {
        TODO("Not yet implemented")
    }
}
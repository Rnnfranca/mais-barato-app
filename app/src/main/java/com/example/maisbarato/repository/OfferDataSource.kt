package com.example.maisbarato.repository

import com.example.maisbarato.model.Oferta
import com.example.maisbarato.repository.local.RepositoryResult

interface OfferDataSource {

    suspend fun getAllOffers(): RepositoryResult<List<Oferta>>

    suspend fun addOfferToHistory(userUid: String, offer: Oferta)
}
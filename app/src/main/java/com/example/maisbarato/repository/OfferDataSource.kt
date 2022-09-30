package com.example.maisbarato.repository

import com.example.maisbarato.model.Oferta
import com.example.maisbarato.repository.local.RepositoryResult
import kotlinx.coroutines.flow.Flow

interface OfferDataSource {

    suspend fun getAllOffers(): RepositoryResult<Flow<List<Oferta>>>

    suspend fun addOfferToHistory(userUid: String, offer: Oferta)
}
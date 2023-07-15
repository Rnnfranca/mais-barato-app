package com.example.maisbarato.data.repository

import com.example.maisbarato.data.model.Oferta
import com.example.maisbarato.data.repository.local.RepositoryResult
import kotlinx.coroutines.flow.Flow

interface OfferDataSource {

    suspend fun getAllOffers(): RepositoryResult<Flow<List<Oferta>>>

    suspend fun addOfferToHistory(userUid: String, offer: Oferta)
}
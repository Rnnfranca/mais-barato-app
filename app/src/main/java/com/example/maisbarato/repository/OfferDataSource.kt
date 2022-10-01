package com.example.maisbarato.repository

import com.example.maisbarato.database.entity.OfferEntity
import com.example.maisbarato.repository.local.RepositoryResult
import kotlinx.coroutines.flow.Flow

interface OfferDataSource {

    suspend fun getAllOffers(): RepositoryResult<Flow<List<OfferEntity>>>

    suspend fun addOfferToHistory(userUid: String, offer: OfferEntity)
}
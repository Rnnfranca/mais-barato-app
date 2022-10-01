package com.example.maisbarato.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.maisbarato.database.entity.OfferEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OfferDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun adicionaOferta(oferta: OfferEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addListOffers(offerList: List<OfferEntity>)

    @Query("SELECT * FROM offer ORDER BY dataInclusao ASC")
    fun readAllOffers(): Flow<List<OfferEntity>>
}
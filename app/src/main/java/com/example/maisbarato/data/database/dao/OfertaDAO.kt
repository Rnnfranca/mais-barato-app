package com.example.maisbarato.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.maisbarato.data.model.Oferta
import kotlinx.coroutines.flow.Flow

@Dao
interface OfertaDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun adicionaOferta(oferta: Oferta)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addListOffers(offerList: List<Oferta>)

    @Query("SELECT * FROM oferta ORDER BY dataInclusao ASC")
    fun readAllOffers(): Flow<List<Oferta>>
}
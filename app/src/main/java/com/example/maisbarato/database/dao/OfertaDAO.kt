package com.example.maisbarato.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.maisbarato.model.Oferta

@Dao
interface OfertaDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun adicionaOferta(oferta: Oferta)

    @Query("SELECT * FROM oferta ORDER BY dataInclusao ASC")
    suspend fun lerTodasOfertas(): List<Oferta>
}
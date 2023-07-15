package com.example.maisbarato.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.maisbarato.data.database.dao.OfertaDAO
import com.example.maisbarato.data.model.Oferta

@Database(
    entities = [
        Oferta::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MaisBaratoDatabase : RoomDatabase() {

    abstract fun ofertaDAO(): OfertaDAO
}
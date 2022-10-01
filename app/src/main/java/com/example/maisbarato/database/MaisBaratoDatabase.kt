package com.example.maisbarato.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.maisbarato.database.dao.OfferDAO
import com.example.maisbarato.database.entity.OfferEntity

@Database(
    entities = [
        OfferEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MaisBaratoDatabase : RoomDatabase() {

    abstract fun offerDAO(): OfferDAO
}
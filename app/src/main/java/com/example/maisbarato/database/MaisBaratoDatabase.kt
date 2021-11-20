package com.example.maisbarato.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.maisbarato.model.Oferta

@Database(entities = [Oferta::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MaisBaratoDatabase : RoomDatabase() {

    abstract fun ofertaDAO(): OfertaDAO

    companion object {
        @Volatile
        private var INSTANCE: MaisBaratoDatabase? = null

        fun getDatabase(context: Context): MaisBaratoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MaisBaratoDatabase::class.java,
                    "funcionario_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
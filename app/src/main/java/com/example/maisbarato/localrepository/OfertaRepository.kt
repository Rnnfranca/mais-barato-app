package com.example.maisbarato.localrepository

import android.util.Log
import com.example.maisbarato.database.OfertaDAO
import com.example.maisbarato.model.Oferta
import com.example.maisbarato.util.OFERTA_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class OfertaRepository @Inject constructor(val ofertaDAO: OfertaDAO) {

    private val firestore = FirebaseFirestore.getInstance()
    private val ofertaCollection = firestore.collection(OFERTA_COLLECTION)

    suspend fun lerTodasOfertas(): List<Oferta> {
        return try {
            ofertaCollection.get().await().toObjects(Oferta::class.java)
        } catch (e: Exception) {
            Log.w("OfertaRepository", e)
            emptyList()
        }
    }

    suspend fun adicionaOferta(oferta: Oferta) = ofertaDAO.adicionaOferta(oferta)

}
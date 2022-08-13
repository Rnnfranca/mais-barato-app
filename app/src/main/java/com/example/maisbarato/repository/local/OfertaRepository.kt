package com.example.maisbarato.repository.local

import android.util.Log
import com.example.maisbarato.database.dao.OfertaDAO
import com.example.maisbarato.model.Oferta
import com.example.maisbarato.util.COLLECTION_OFERTA
import com.example.maisbarato.util.COLLECTION_USUARIO
import com.example.maisbarato.util.SUBCOLLECTION_HISTORY
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class OfertaRepository @Inject constructor(val ofertaDAO: OfertaDAO) {

    private val TAG = OfertaRepository::class.java.simpleName

    private val firestore = FirebaseFirestore.getInstance()
    private val ofertaCollection = firestore.collection(COLLECTION_OFERTA)
    private val usuarioCollectionRef = firestore.collection(COLLECTION_USUARIO)

    suspend fun lerTodasOfertas(): List<Oferta> {
        return try {
            ofertaCollection.get()
                .await()
                .toObjects(Oferta::class.java)
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            emptyList()
        }
    }

    suspend fun getOfferHistory(userUid: String): List<Oferta> {
        return try {
            usuarioCollectionRef.document(userUid)
                .collection(SUBCOLLECTION_HISTORY)
                .get()
                .await()
                .toObjects(Oferta::class.java)
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            emptyList()
        }
    }

    suspend fun addOfferToHistory(userUid: String, offer: Oferta) {
        try {
            usuarioCollectionRef.document(userUid)
                .collection(SUBCOLLECTION_HISTORY)
                .document(offer.id)
                .set(offer)
                .await()
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
        }
    }

}
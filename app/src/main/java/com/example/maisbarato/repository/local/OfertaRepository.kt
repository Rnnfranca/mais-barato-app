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
        validateInsertHistory(userUid, offer)
    }

    private suspend fun validateInsertHistory(userUid: String, offer: Oferta) {
        try {
            val offerHistory = getOfferHistory(userUid)

            if (offerHistory.size < 10) {
                insertHistory(userUid, offer)
            } else {

                val lastOfferHistory = offerHistory.maxByOrNull {
                    it.dataAcesso ?: 0L
                }

                lastOfferHistory?.also {
                    deleteOfferFromHistory(userUid, it)
                }

                insertHistory(userUid, offer)
            }

        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
        }
    }

    private suspend fun insertHistory(userUid: String, offer: Oferta) {
        usuarioCollectionRef.document(userUid)
            .collection(SUBCOLLECTION_HISTORY)
            .document(offer.id)
            .set(offer)
            .await()
    }

    suspend fun deleteOfferFromHistory(userUid: String, offer: Oferta) {
        usuarioCollectionRef.document(userUid)
            .collection(SUBCOLLECTION_HISTORY)
            .document(offer.id)
            .delete()
            .await()
    }

}
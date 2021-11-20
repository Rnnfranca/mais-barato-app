package com.example.maisbarato.remotedatabase

import android.util.Log
import com.example.maisbarato.model.Oferta
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MaisBaratoDatabaseFirestore {
    val TAG = "MaisBaratoDatabase"
    val db = Firebase.firestore.collection("oferta")

    fun adicionaOfertaFirestore(oferta: Oferta) {
        db.add(oferta)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

//    suspend fun lerTodasOfertas() {
//        ofertaCollection.get()
//            .await()
//            .toObjects(Oferta::class.java).also {
//                println(it)
//            }
//    }
}
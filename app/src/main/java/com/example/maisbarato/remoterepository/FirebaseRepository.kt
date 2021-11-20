package com.example.maisbarato.remoterepository

import android.net.Uri
import android.util.Log
import com.example.maisbarato.model.Oferta
import com.example.maisbarato.util.OFERTA_COLLECTION
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class FirebaseRepository {
    private val databaseFirestore = Firebase.firestore
    private val ofertaCollectionRef = databaseFirestore.collection(OFERTA_COLLECTION)

    private val storage = Firebase.storage
    private var storageRef = storage.reference

    private fun salvarNoFirebase(oferta: Oferta) {
        val document = ofertaCollectionRef.document()
        oferta.id = document.id

        ofertaCollectionRef.document(oferta.id).set(oferta)
            .addOnSuccessListener {
                Log.d("OfertaCollection", "DocumentSnapshot added with ID: $it")
            }
            .addOnFailureListener {
                Log.w("OfertaCollection", "Error adding document", it)
            }
    }

    fun salvaOferta(oferta: Oferta) {
        val fileUri = oferta.listaUrlImagem.map { Uri.parse(it) }

        val urlStrings = mutableListOf<String>()

        fileUri.forEach { uri ->
            val imagemRef = storageRef.child("imagens/${uri.lastPathSegment}")
            val uploadTask = imagemRef.putFile(uri)

            uploadTask.addOnFailureListener {
                Log.d("OfertaCollection", "Falha ao salvar")
            }.addOnSuccessListener {
                imagemRef.downloadUrl.addOnSuccessListener {
                    urlStrings.add(it.toString())

                    if (urlStrings.size == fileUri.size) {
                        oferta.listaUrlImagem = urlStrings
                        salvarNoFirebase(oferta)
                    }
                }

                Log.d("OfertaCollection", "SALVOUUUUU")
            }
        }
    }
}
package com.example.maisbarato.remoterepository

import android.app.Application
import android.net.Uri
import android.util.Log
import com.example.maisbarato.localrepository.RepositoryResult
import com.example.maisbarato.model.Oferta
import com.example.maisbarato.util.COLLECTION_OFERTA
import com.example.maisbarato.util.COLLECTION_USUARIO
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class FirebaseRepository(application: Application) {

    private val TAG = FirebaseRepository::class.java.name

    private val databaseFirestore = Firebase.firestore
    private val ofertaCollectionRef = databaseFirestore.collection(COLLECTION_OFERTA)
    private val usuarioCollectionRef = databaseFirestore.collection(COLLECTION_USUARIO)

    private val storage = Firebase.storage
    private var storageRef = storage.reference

    private fun salvarNoFirebase(oferta: Oferta) {
        val document = ofertaCollectionRef.document()
        oferta.id = document.id

        ofertaCollectionRef.document(oferta.id).set(oferta)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot added with ID: $it")
            }
            .addOnFailureListener {
                Log.w(TAG, "Error adding document", it)
            }
    }

    fun salvaImagemUsuario(uri: Uri, uidUsuario: String, salvaNoDatastore: ((String) -> Unit)) {
        Log.d("TEESTE", "salvaImagemUsuario")
        try {
            Log.d("TEESTE", "try")
            val imagemRef = storageRef.child("imagens/usuarios/${uri.lastPathSegment}")
            val uploadTask = imagemRef.putFile(uri)

            uploadTask.addOnSuccessListener {
                imagemRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    Log.d("TEESTE", "addOnSuccessListener")
//                    salvarImagemNaColecao(downloadUrl.toString(), uidUsuario)

                    salvaNoDatastore.invoke(downloadUrl.toString())
                }
            }

        } catch (e: Exception) {
            Log.e(TAG, e.printStackTrace().toString())
        }
    }

    private fun salvarImagemNaColecao(url: String, uidUsuario: String) {
        val data = hashMapOf("urlImagemPerfil" to url)

        usuarioCollectionRef.document(uidUsuario)
            .set(data, SetOptions.merge())
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot added with ID: $it")
            }
            .addOnFailureListener {
                Log.w(TAG, "Error adding document", it)
            }

    }

    fun salvaOferta(oferta: Oferta): RepositoryResult<String> {
        val fileUri = oferta.listaUrlImagem.map { Uri.parse(it) }

        val urlStrings = mutableListOf<String>()

        return try {
            fileUri.forEach { uri ->
                val imagemRef = storageRef.child("imagens/${uri.lastPathSegment}")
                val uploadTask = imagemRef.putFile(uri)

                uploadTask.addOnSuccessListener {
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
            RepositoryResult.Success(
                "Sucesso"
            )
        } catch (e: Exception) {
            RepositoryResult.Error(
                "Falha ao salvar"
            )
        }

    }
}
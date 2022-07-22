package com.example.maisbarato.repository.firebase

import android.net.Uri
import android.util.Log
import com.example.maisbarato.model.Oferta
import com.example.maisbarato.model.Usuario
import com.example.maisbarato.repository.local.RepositoryResult
import com.example.maisbarato.util.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class FirebaseRepository() {

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

    fun uploadImageUser(uri: Uri, savedImageUrl: ((String) -> Unit)) {

        try {
            val imagemRef = storageRef.child("$PATH_IMAGENS/$PATH_USUARIOS/${uri.lastPathSegment}")
            val uploadTask = imagemRef.putFile(uri)

            uploadTask.addOnSuccessListener {
                imagemRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    savedImageUrl.invoke(downloadUrl.toString())
                }
            }

        } catch (e: Exception) {
            Log.e(TAG, e.printStackTrace().toString())
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

    fun getUserInfo(userUid: String, userInfo: (Usuario) -> Unit) {
        usuarioCollectionRef.document(userUid)
            .get()
            .addOnSuccessListener {
                it.data?.also { usuario ->

                    userInfo.invoke(
                        Usuario(
                            nome = usuario[FIELD_NOME].toString(),
                            email = usuario[FIELD_EMAIL].toString(),
                            telefone = usuario[FIELD_TELEFONE].toString()
                        )
                    )
                }
            }
    }

    fun updateUserInfo(uidUsuario: String, fullName: String, email: String, imageUri: String?, updated: (Boolean, String) -> Unit) {

        uploadImageUser(Uri.parse(imageUri)) { urlImage ->
            val data = hashMapOf(
                FIELD_EMAIL to email,
                FIELD_NOME to fullName,
                FIELD_URL_IMAGEM_PERFIL to urlImage
            )

            usuarioCollectionRef.document(uidUsuario)
                .set(data)
                .addOnSuccessListener {
                    updated.invoke(true, urlImage)
                }
                .addOnFailureListener {
                    updated.invoke(false, urlImage)
                    Log.e(TAG, it.message.toString())
                }
        }

    }

}
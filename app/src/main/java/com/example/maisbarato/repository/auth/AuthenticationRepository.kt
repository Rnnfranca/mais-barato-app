package com.example.maisbarato.repository.auth

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

object AuthenticationRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    fun login(email: String, senha: String) : Task<AuthResult> =
        auth.signInWithEmailAndPassword(email, senha)

    fun createUser(email: String, senha: String) : Task<AuthResult> =
        auth.createUserWithEmailAndPassword(email, senha)

    fun signOut() {
        auth.signOut()
    }
}
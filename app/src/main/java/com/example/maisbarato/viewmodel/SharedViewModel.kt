package com.example.maisbarato.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseAuth

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = SharedViewModel::class.java.name

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    val currentUser get() = auth.currentUser

    fun logout() {
        auth.signOut()
    }
}
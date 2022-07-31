package com.example.maisbarato.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.maisbarato.model.Oferta
import com.example.maisbarato.repository.auth.AuthenticationRepository
import com.example.maisbarato.repository.firebase.FirebaseRepository
import com.example.maisbarato.util.StateViewResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    val firebaseRepository = FirebaseRepository()

    private val _stateView: MutableStateFlow<StateViewResult<List<Oferta>>?> = MutableStateFlow(null)
    val stateView = _stateView.asStateFlow()

    val userUid = AuthenticationRepository.currentUser?.uid ?: ""

    fun getFavorites(dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        viewModelScope.launch(dispatcher) {
            _stateView.value = StateViewResult.Loading

            val listFavorites = firebaseRepository.getFavorites(userUid)

            if (listFavorites.isNotEmpty()) {
                _stateView.value = StateViewResult.Success(result = listFavorites)
            } else {
                _stateView.value = StateViewResult.Error()
            }
        }
    }
}
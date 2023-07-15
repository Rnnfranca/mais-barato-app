package com.example.maisbarato.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.maisbarato.data.model.Oferta
import com.example.maisbarato.data.repository.auth.AuthenticationRepository
import com.example.maisbarato.data.repository.firebase.FirebaseRepository
import com.example.maisbarato.util.StateViewResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    application: Application,
    authRepository: AuthenticationRepository
) : AndroidViewModel(application) {

    val firebaseRepository = FirebaseRepository()

    private val _stateView: MutableStateFlow<StateViewResult<List<Oferta>>?> = MutableStateFlow(null)
    val stateView = _stateView.asStateFlow()

    val userUid = authRepository.currentUser?.uid ?: ""

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
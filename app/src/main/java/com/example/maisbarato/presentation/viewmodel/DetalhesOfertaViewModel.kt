package com.example.maisbarato.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.maisbarato.data.model.Oferta
import com.example.maisbarato.data.model.Usuario
import com.example.maisbarato.data.repository.auth.AuthenticationRepository
import com.example.maisbarato.data.repository.firebase.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetalhesOfertaViewModel @Inject constructor(
    application: Application,
    authRepository: AuthenticationRepository
) : AndroidViewModel(application) {

    val firebaseRepository = FirebaseRepository()

    private val _isFavorite: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isFavorite = _isFavorite.asStateFlow()

    private val _userInfo: MutableStateFlow<Usuario?> = MutableStateFlow(null)
    val userInfo get() = _userInfo.asStateFlow()

    private var userUid = authRepository.currentUser?.uid ?: ""

    fun saveOrRemoveFavorite(oferta: Oferta) {
        if (isFavorite.value) {
            removeFavorite(oferta.id)
        } else {
            saveFavorite(oferta)
        }
    }

    private fun removeFavorite(offerId: String, dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        viewModelScope.launch(dispatcher) {
            firebaseRepository.removeFavorite(userUid, offerId) {
                _isFavorite.value = !it
            }
        }
    }

    private fun saveFavorite(offer: Oferta, dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        viewModelScope.launch(dispatcher) {
            firebaseRepository.saveFavoriteOffer(userUid, offer) { saved ->
                _isFavorite.value = saved
            }
        }
    }

    fun verifyFavorite(ofertaId: String, dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        viewModelScope.launch(dispatcher) {
            firebaseRepository.verifyFavorite(userUid, ofertaId) {
                _isFavorite.value = it
            }
        }
    }

    fun getUserOffersInfos(uidUserOffer: String, dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        viewModelScope.launch(dispatcher) {
            firebaseRepository.getUserInfo(uidUserOffer)?.also { user ->
                _userInfo.emit(user)
            }
        }
    }

}
package com.example.maisbarato.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.maisbarato.model.Oferta
import com.example.maisbarato.repository.auth.AuthenticationRepository
import com.example.maisbarato.repository.firebase.FirebaseRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetalhesOfertaViewModel(application: Application) :
    AndroidViewModel(application) {

    val firebaseRepository = FirebaseRepository()

    private val _isFavorite: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isFavorite = _isFavorite.asStateFlow()

    private var userUid = AuthenticationRepository.currentUser?.uid ?: ""

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

    fun getUserOffersInfos(uidUserOffer: String, dispatcher: CoroutineDispatcher = Dispatchers.IO, user: (userName: String?, userImage: String?) -> Unit) {
        viewModelScope.launch(dispatcher) {
            firebaseRepository.getUserInfo(uidUserOffer) {
                user.invoke(it.nome, it.urlImagePerfil)
            }
        }
    }

}
package com.example.maisbarato.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.maisbarato.repository.OfferRepositoryFake
import com.example.maisbarato.data.repository.auth.AuthenticationRepository
import com.example.maisbarato.util.getOrAwaitValue
import com.nhaarman.mockitokotlin2.mock
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ListaOfertasViewModelTest() {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val authRepositoryFake = mock<AuthenticationRepository>()

    @Test
    fun loadOffers_setsOfferEvent() {
        // given
        val listOfferViewModel = ListaOfertasViewModel(OfferRepositoryFake(), authRepositoryFake)

        // when
        listOfferViewModel.lerTodasOfertas()
        val value = listOfferViewModel.oferta.getOrAwaitValue()

        // then
        assertThat(value, not(nullValue()))
        assertTrue(value.size > 1)
    }
}
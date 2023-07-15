package com.example.maisbarato.data.repository.local

sealed class RepositoryResult<out t> {
    data class Success<T>(val result: T) : RepositoryResult<T>()
    data class Error(val error: String = "") : RepositoryResult<Nothing>()
}
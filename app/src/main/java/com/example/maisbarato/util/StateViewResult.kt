package com.example.maisbarato.util

sealed class StateViewResult<out T> {
    object Loading: StateViewResult<Nothing>()
    data class Error(var errorMsg: String = ""): StateViewResult<Nothing>()
    data class Success<T>(var result: T? = null): StateViewResult<T>()
}
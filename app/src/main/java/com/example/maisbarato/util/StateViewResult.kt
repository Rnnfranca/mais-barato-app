package com.example.maisbarato.util

sealed class StateViewResult<out T: Any> {
    object Initial : StateViewResult<Nothing>()
    object Loading: StateViewResult<Nothing>()
    data class Error(var errorMsg: String = ""): StateViewResult<Nothing>()
    data class Success<T: Any>(var result: T? = null): StateViewResult<T>()
}
package com.example.mm_inventory.model.data.response

sealed class Response<out T> {
    object Loading: Response<Nothing>()

    data class Success<out T>(
        val data: T
    ): Response<T>()

    data class Error(
        val e: String
    ): Response<Nothing>()
}

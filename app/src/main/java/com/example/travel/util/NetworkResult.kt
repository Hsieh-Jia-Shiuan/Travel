package com.example.travel.util

/**
 * @param T 資料類型。
 * @param data 成功時的資料。
 * @param message 錯誤時的訊息。
 */
sealed class NetworkResult<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(message: String, data: T? = null) : NetworkResult<T>(data, message)
    class Loading<T> : NetworkResult<T>()
}
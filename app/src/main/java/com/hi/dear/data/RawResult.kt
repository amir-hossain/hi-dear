package com.hi.dear.data

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class RawResult<out T : Any> {

    data class Success<out T : Any>(val data: T) : RawResult<T>()
    data class Error(val exception: Exception) : RawResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}
package de.rogallab.mobile.domain

sealed interface ResultData<out T> {
   data class Success<out T>(val data: T) : ResultData<T>
   data class Failure(val throwable: Throwable) : ResultData<Nothing>
}
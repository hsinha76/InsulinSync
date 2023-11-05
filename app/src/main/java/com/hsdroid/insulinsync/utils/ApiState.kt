package com.hsdroid.insulinsync.utils

sealed class ApiState<out T> {
    object EMPTY : ApiState<Nothing>()
    object LOADING : ApiState<Nothing>()
    class SUCCESS<out T>(val data: T) : ApiState<T>()
    class FAILURE(val throwable: Throwable) : ApiState<Nothing>()
}

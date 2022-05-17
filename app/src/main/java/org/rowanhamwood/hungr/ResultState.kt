package org.rowanhamwood.hungr

sealed class ResultState {
    object Success: ResultState()
    data class Failure(val message: String): ResultState()
    object Loading: ResultState()
}
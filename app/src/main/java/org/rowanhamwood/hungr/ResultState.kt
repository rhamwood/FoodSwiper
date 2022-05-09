package org.rowanhamwood.hungr

sealed class ResultState {
    object Success: ResultState() // this is object because I added no params
    data class Failure(val message: String): ResultState()
}
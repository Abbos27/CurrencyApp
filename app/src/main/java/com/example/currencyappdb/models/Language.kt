package com.example.currencyappdb.models

data class Language(
    var image: Int,
    var langName: String,
    var isChosen: Boolean = false
) {
}
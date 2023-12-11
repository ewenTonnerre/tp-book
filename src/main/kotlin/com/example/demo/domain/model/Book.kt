package com.example.demo.domain.model

data class Book(val title: String, val author: String, val reserved: Boolean = false) {
    init {
        require(title.isNotBlank()) { "Title cannot be empty" }
        require(author.isNotBlank()) { "Author cannot be empty" }
    }
}
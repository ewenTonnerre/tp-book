package com.example.demo.domain.port

import com.example.demo.domain.model.Book

interface BookPort {
    fun createBook(title: String, author: String) : Book
    fun list() : List<Book>
}
package com.example.demo.domain.port

import com.example.demo.domain.model.Book

interface BookPort {
    fun createBook(book: Book)
    fun list() : List<Book>
}
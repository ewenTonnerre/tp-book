package com.example.demo.domain.port

import com.example.demo.domain.model.Book

interface BookPort {
    fun createBook(book: Book)
    fun list() : List<Book>
    fun reserveBook(bookTitle: String)
    fun getBookByTitle(bookTitle: String) : Book?
}
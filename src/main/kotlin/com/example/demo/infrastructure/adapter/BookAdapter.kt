package com.example.demo.infrastructure.adapter

import com.example.demo.domain.model.Book
import com.example.demo.domain.port.BookPort

class BookAdapter : BookPort {

    private val books = mutableListOf<Book>()

    override fun createBook(title: String, author: String): Book {
        val book = Book(title, author)
        books.add(book)
        return book
    }

    override fun list(): List<Book> = books
}
package com.example.demo.domain.usecase

import com.example.demo.domain.model.Book
import com.example.demo.domain.port.BookPort

class BookUsecase(private val bookPort: BookPort) {
    fun createBook(title: String, author: String) : Book{
        return bookPort.createBook(title, author)
    }

    fun listBooks() : List<Book>{
        return bookPort.list().sortedBy { it.title.replaceFirstChar {char -> char.uppercase() } }
    }
}
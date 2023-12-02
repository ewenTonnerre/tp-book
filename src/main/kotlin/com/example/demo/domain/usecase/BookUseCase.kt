package com.example.demo.domain.usecase

import com.example.demo.domain.model.Book
import com.example.demo.domain.port.BookPort

class BookUseCase(private val bookPort: BookPort) {
    fun createBook(book: Book) {
        bookPort.createBook(book)
    }

    fun listBooks() : List<Book>{
        return bookPort.list().sortedBy { it.title.replaceFirstChar {char -> char.uppercase() } }
    }
}
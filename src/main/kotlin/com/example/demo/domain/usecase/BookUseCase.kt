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

    // TP
    fun reserveBook(bookTitle: String) {
        val book = bookPort.getBookByTitle(bookTitle) ?: throw Exception("Book doesn't exist")

        if (book.reserved){
            throw Exception("Book is already reserved")
        } else {
            bookPort.reserveBook(bookTitle)
        }
    }
}
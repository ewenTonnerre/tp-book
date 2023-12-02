package com.example.demo.infrastructure.driver.controller

import com.example.demo.domain.model.Book
import com.example.demo.domain.usecase.BookUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/books")
class BookController(private var bookUseCase: BookUseCase) {
    @GetMapping
    fun getList() : List<Book>{
        return bookUseCase.listBooks()
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody book: Book ){
        bookUseCase.createBook(book)
    }
}
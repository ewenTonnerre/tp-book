package com.example.demo.infrastructure.driver.controller

import com.example.demo.domain.model.Book
import com.example.demo.domain.usecase.BookUseCase
import com.example.demo.infrastructure.driver.dto.BookDTO
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/books")
class BookController(private var bookUseCase: BookUseCase) {
    @CrossOrigin
    @GetMapping
    fun getList() : List<Book>{
        return bookUseCase.listBooks()
    }

    @CrossOrigin
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody bookDTO: BookDTO ){
        bookUseCase.createBook(bookDTO.toDomain())
    }

    @CrossOrigin
    @PatchMapping("/reserve")
    @ResponseStatus(HttpStatus.CREATED)
    fun reserve(@RequestBody body: Map<String, Any> ){
        bookUseCase.reserveBook(body["bookTitle"] as String)
    }
}
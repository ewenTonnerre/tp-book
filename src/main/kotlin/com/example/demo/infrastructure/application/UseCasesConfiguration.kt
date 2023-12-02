package com.example.demo.infrastructure.application

import com.example.demo.domain.usecase.BookUseCase
import com.example.demo.infrastructure.driven.adapter.BookDAO
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UseCasesConfiguration {
    @Bean
    fun bookUseCase(bookDAO: BookDAO): BookUseCase {
        return BookUseCase(bookDAO)
    }
}
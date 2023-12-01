package com.example.demo.domain.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class BookTest {
    @Test
    fun `a book has a title and an author`() {
        val title = "La ligne verte"
        val author = "Stephen King"

        val book = Book(title, author);

        assertThat(book.title).isEqualTo("La ligne verte")
        assertThat(book.author).isEqualTo("Stephen King")
    }
}
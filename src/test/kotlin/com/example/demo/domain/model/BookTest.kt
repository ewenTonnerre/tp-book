package com.example.demo.domain.model

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import org.junit.jupiter.api.Test

class BookTest {
    @Test
    fun `book name cannot be blank`() {
        assertFailure { Book("", "Victor Hugo") }
                .isInstanceOf(IllegalArgumentException::class)
                .hasMessage("Title cannot be empty")
    }

    @Test
    fun `book author cannot be blank`() {
        assertFailure { Book("Les misérables", "") }
                .isInstanceOf(IllegalArgumentException::class)
                .hasMessage("Author cannot be empty")
    }
}
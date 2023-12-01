package com.example.demo.domain.usecase

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import com.example.demo.infrastructure.adapter.BookAdapter
import net.jqwik.api.*
import org.junit.jupiter.api.Test

class BookAdapterTest {
    @Test
    fun `test create book`() {
        val title = "La ligne verte"
        val author = "Stephen King"

        val bookAdapter = BookAdapter()
        val book = bookAdapter.createBook(title, author)

        assertThat(book.title).isEqualTo(title)
        assertThat(book.author).isEqualTo(author)
    }

    @Test
    fun `test list books`() {
        val bookAdapter = BookAdapter()
        val book1 = bookAdapter.createBook("Trust", "Franck")
        val book2 = bookAdapter.createBook("Avatar", "James")
        val book3 = bookAdapter.createBook("Karate kid", "Denis")

        val books = bookAdapter.list()

        assertThat(books).containsExactly(book1, book2, book3)
    }
}

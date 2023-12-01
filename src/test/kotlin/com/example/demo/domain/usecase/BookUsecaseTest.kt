package com.example.demo.domain.usecase

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import com.example.demo.domain.model.Book
import com.example.demo.domain.port.BookPort
import com.example.demo.infrastructure.adapter.BookAdapter
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import net.jqwik.api.*
import net.jqwik.api.constraints.Size
import net.jqwik.api.constraints.UniqueElements
import net.jqwik.api.lifecycle.BeforeProperty
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith


@ExtendWith(MockKExtension::class)
class BookUsecaseTest {

    @MockK
    private lateinit var mockBookPort: BookPort

    @Test
    fun `test create book call bookPort`() {
        val title = "La ligne verte"
        val author = "Stephen King"
        every { mockBookPort.createBook(title, author) } returns Book(title, author)

        val bookUsecase = BookUsecase(mockBookPort)
        bookUsecase.createBook(title, author)

        verify(exactly = 1) {mockBookPort.createBook(title, author)}
    }

    @Test
    fun `test create book return book`() {
        val title = "La ligne verte"
        val author = "Stephen King"

        val bookUsecase = BookUsecase(BookAdapter())
        val book = bookUsecase.createBook(title, author)
        assertThat(book.title).isEqualTo("La ligne verte")
        assertThat(book.author).isEqualTo("Stephen King")
    }

    @Test
    fun `test list books call bookPort`() {
        val bookUsecase = BookUsecase(mockBookPort)
        every { mockBookPort.list() } returns listOf<Book>(Book("title", "author"))
        bookUsecase.listBooks()

        verify(exactly = 1) {mockBookPort.list()}
    }

    @Test
    fun `test list books order books by title`() {
        val bookUsecase = BookUsecase(BookAdapter())
        val book1 = bookUsecase.createBook("Trust", "Franck")
        val book2 = bookUsecase.createBook("avatar", "James")
        val book3 = bookUsecase.createBook("Karate kid", "Denis")

        val books = bookUsecase.listBooks()

        assertThat(books).containsExactly(book2, book3, book1)
    }

    @Test
    fun `test list books with a lowercase ordered by title`() {
        val bookUsecase = BookUsecase(BookAdapter())
        val book1 = bookUsecase.createBook("Trust", "Franck")
        val book2 = bookUsecase.createBook("avatar", "James")
        val book3 = bookUsecase.createBook("Karate kid", "Denis")

        val books = bookUsecase.listBooks()

        assertThat(books).containsExactly(book2, book3, book1)
    }

    @Property
    fun `test create book property`(
            @ForAll title: String,
            @ForAll author: String) {

        val bookAdapter = BookAdapter()
        val book = bookAdapter.createBook(title, author)

        assertThat(book.title).isEqualTo(title)
        assertThat(book.author).isEqualTo(author)
    }

    /*@Property
    fun `test list books property`(
            @ForAll @Size(5)
            @UniqueElements books: List<@From("book") Book>) {

        val bookUsecase = BookUsecase(mockBookPort)

        every { mockBookPort.list() } returns books

        val listBooks = bookUsecase.listBooks()

        assertThat(listBooks).hasSize(books.size)
        assertThat(listBooks).isEqualTo(books.sortedBy { it.title.replaceFirstChar { char -> char.uppercase() } })
    }*/

    @Provide
    fun book(): Arbitrary<Book> {
        return Arbitraries.forType(Book::class.java)
    }
}

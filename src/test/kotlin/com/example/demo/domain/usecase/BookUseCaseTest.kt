package com.example.demo.domain.usecase

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.containsExactlyInAnyOrder
import assertk.assertions.hasMessage
import assertk.assertions.isInstanceOf
import com.example.demo.domain.model.Book
import com.example.demo.domain.port.BookPort
import com.example.demo.infrastructure.driven.adapter.BookDAO
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import net.jqwik.api.*
import net.jqwik.api.Combinators.combine
import net.jqwik.api.constraints.NotBlank
import net.jqwik.api.lifecycle.BeforeProperty
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith


@ExtendWith(MockKExtension::class)
class BookUseCaseTest {

    @InjectMockKs
    private lateinit var bookUseCase: BookUseCase

    @MockK
    private lateinit var mockBookPort: BookPort

    // TP
    @Test
    fun `reserve book`() {
        val book = Book("A", "B", false)
        justRun { mockBookPort.reserveBook(any()) }
        every { mockBookPort.getBookByTitle(any()) } returns book

        bookUseCase.reserveBook("A")

        verify(exactly = 1) { mockBookPort.getBookByTitle("A") }
        verify(exactly = 1) { mockBookPort.reserveBook(book.title) }
    }

    // TP
    @Test
    fun `book doesn't exist`() {
        justRun { mockBookPort.reserveBook(any()) }
        every { mockBookPort.getBookByTitle(any()) } returns null

        assertFailure { bookUseCase.reserveBook("La ligne verte") }
                .isInstanceOf(Exception::class)
                .hasMessage("Book doesn't exist")
    }

    // TP
    @Test
    fun `book already reserved`() {
        val book = Book("A", "B", true)

        justRun { mockBookPort.reserveBook(any()) }
        every { mockBookPort.getBookByTitle(any()) } returns book

        assertFailure { bookUseCase.reserveBook("A") }
                .isInstanceOf(Exception::class)
                .hasMessage("Book is already reserved")
    }

    @Test
    fun `add book`() {
        justRun { mockBookPort.createBook(any()) }

        val book = Book("Les Misérables", "Victor Hugo")

        bookUseCase.createBook(book)

        verify(exactly = 1) { mockBookPort.createBook(book) }
    }

    @Test
    fun `get all books should returns all books sorted by name`() {
        every { mockBookPort.list() } returns listOf(
                Book("Tenet", "Jean"),
                Book("Avatar", "James")
        )

        val res = bookUseCase.listBooks()

        assertThat(res).containsExactly(
                Book("Avatar", "James"),
                Book("Tenet", "Jean")
        )
    }

    @Test
    fun `test list books with a lowercase ordered by title`() {
        val book1 = Book("Trust", "Franck")
        val book2 = Book("avatar", "James")
        val book3 = Book("Karate kid", "Denis")
        every { mockBookPort.list() } returns listOf(
                book1,
                book2,
                book3
        )

        val books = bookUseCase.listBooks()

        assertThat(books).containsExactly(book2, book3, book1)
    }

    @BeforeProperty
    fun initMocks() {
        MockKAnnotations.init(this)
    }

    @Property
    fun `get all all book should have all books stored in db`(
            @ForAll("bookGenerator") books: List<Book>
    ) {
        every { mockBookPort.list() } returns books

        val res = bookUseCase.listBooks()

        assertThat(res).containsExactlyInAnyOrder(*books.toTypedArray())
    }

    @Provide
    fun bookGenerator(): Arbitrary<List<Book>> {
        return combine(
                Arbitraries.strings().ofMinLength(1).ofMaxLength(20).alpha(),
                Arbitraries.strings().ofMinLength(1).ofMaxLength(20).alpha()).`as` { title: String, author: String ->
            Book(title, author)
        }.list().uniqueElements().ofSize(10)
    }
}

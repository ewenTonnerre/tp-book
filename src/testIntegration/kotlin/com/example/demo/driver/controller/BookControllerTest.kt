package com.example.demo.driver.controller

import com.example.demo.domain.model.Book
import com.example.demo.domain.usecase.BookUseCase
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.justRun
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post

@ExtendWith(SpringExtension::class)
@WebMvcTest
class BookControllerTest {
    @MockkBean
    private lateinit var bookUseCase: BookUseCase

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `get books`() {
        // GIVEN
        every { bookUseCase.listBooks() } returns listOf(
                Book("Avatar", "James"),
                Book("Tenet", "Jean"),
        )

        //WHEN
        mockMvc.get("/books")
        //THEN
        .andExpect{
            status { isOk() }
            content { content { MediaType.APPLICATION_JSON } }
            content { json(
                    """
                        [
                            {
                              "title": "Avatar",
                              "author": "James",
                              "reserved": false
                            },
                            {
                              "title": "Tenet",
                              "author": "Jean",
                              "reserved": false
                            }
                        ]
                    """.trimIndent()
            ) }
        }
    }

    @Test
    fun `create book`() {
        justRun { bookUseCase.createBook(any()) }

        mockMvc.post("/books") {
            // language=json
            content = """
                {
                  "title": "title",
                  "author": "author",
                  "reserved": true
                }
            """.trimIndent()
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect{
            status { isCreated() }
        }

        val expected = Book(
                title = "title",
                author = "author",
                reserved = true
        )

        verify(exactly = 1) { bookUseCase.createBook(expected) }
    }

    @Test
    fun `create book missing field`() {
        justRun { bookUseCase.createBook(any()) }

        mockMvc.post("/books") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect{
            status { isBadRequest() }
        }

        verify(exactly = 0) { bookUseCase.createBook(any()) }
    }

    @Test
    fun `reserve book`() {
        justRun { bookUseCase.reserveBook(any()) }

        mockMvc.patch("/books/reserve") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                            {
                              "bookTitle": "Avatar"
                            }
                    """.trimIndent()
        }.andExpect{
            status { isCreated() }
        }

        verify(exactly = 1) { bookUseCase.reserveBook("Avatar") }
    }

    @Test
    fun `reserve book missing field`() {
        justRun { bookUseCase.reserveBook(any()) }

        mockMvc.patch("/books/reserve") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect{
            status { isBadRequest() }
        }

        verify(exactly = 0) { bookUseCase.reserveBook(any()) }
    }
}
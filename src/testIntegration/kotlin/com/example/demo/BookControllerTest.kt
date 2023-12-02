package com.example.demo

import com.example.demo.domain.model.Book
import com.example.demo.domain.usecase.BookUseCase
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.justRun
import jdk.jshell.spi.ExecutionControl.InternalException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
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
        every { bookUseCase.listBooks() } returns listOf(
                Book("Avatar", "James"),
                Book("Tenet", "Jean"),
        )

        mockMvc.get("/books").andExpect{
            status { isOk() }
            content { content { MediaType.APPLICATION_JSON } }
            content { json(
                    """
                        [
                            {
                              "title": "Avatar",
                              "author": "James"
                            },
                            {
                              "title": "Tenet",
                              "author": "Jean"
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
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(Book("title","author"))
        }.andExpect{
            status { isCreated() }
        }
    }

    @Test
    fun `create book missing field`() {
        justRun { bookUseCase.createBook(any()) }

        mockMvc.post("/books") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect{
            status { isBadRequest() }
        }
    }

    @Test
    fun `create book server error`() {
        every { bookUseCase.createBook(any()) }.throws(Exception("error"))

        mockMvc.post("/books") {
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(Book("title","author"))
        }.andExpect{
            status { isInternalServerError() }
        }
    }
}
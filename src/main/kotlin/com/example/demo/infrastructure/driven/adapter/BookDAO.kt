package com.example.demo.infrastructure.driven.adapter

import com.example.demo.domain.model.Book
import com.example.demo.domain.port.BookPort
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service

@Service
class BookDAO(private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : BookPort {
    override fun createBook(book: Book) {
        namedParameterJdbcTemplate.update("INSERT INTO book (title, author, reserved) values (:title, :author, :reserved)", mapOf(
                "title" to book.title,
                "author" to book.author,
                "reserved" to book.reserved
        ))
    }

    override fun list(): List<Book>{
        return namedParameterJdbcTemplate
                .query("SELECT * FROM book", MapSqlParameterSource()) { rs, _ ->
                    Book(
                            title = rs.getString("title"),
                            author = rs.getString("author"),
                            reserved = rs.getBoolean("reserved")
                    )
                }
    }

    override fun reserveBook(bookTitle: String) {
        namedParameterJdbcTemplate.update(
                "UPDATE book SET reserved = true WHERE title = :title",
                mapOf("title" to bookTitle)
        )
    }

    override fun getBookByTitle(bookTitle: String) : Book? {
        return namedParameterJdbcTemplate.queryForObject(
                "SELECT * FROM book WHERE title = :title",
                mapOf("title" to bookTitle)
        ) { rs, _ ->
            Book(
                    title = rs.getString("title"),
                    author = rs.getString("author"),
                    reserved = rs.getBoolean("reserved")
            )
        }
    }
}
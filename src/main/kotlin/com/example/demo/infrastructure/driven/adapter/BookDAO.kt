package com.example.demo.infrastructure.driven.adapter

import com.example.demo.domain.model.Book
import com.example.demo.domain.port.BookPort
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service

@Service
class BookDAO(private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : BookPort {
    override fun createBook(book: Book) {
        namedParameterJdbcTemplate.update("INSERT INTO book (title, author)", mapOf(
                "title" to book.title,
                "author" to book.author
        ))
    }

    override fun list(): List<Book>{
        return namedParameterJdbcTemplate.query("SELECT * FROM book", MapSqlParameterSource()) {
            res, _ -> Book(res.getString("title"), res.getString("author"))
        }
    }
}
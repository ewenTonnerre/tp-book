package com.example.demo.driven.adapter

import assertk.assertThat
import assertk.assertions.containsExactlyInAnyOrder
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isTrue
import com.example.demo.domain.model.Book
import com.example.demo.infrastructure.driven.adapter.BookDAO
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.sql.ResultSet

@SpringBootTest
@Testcontainers
@ActiveProfiles("testIntegration")
class BookDaoTest {

    @Autowired
    private lateinit var bookDAO: BookDAO

    @BeforeEach
    fun beforeEach() {
        performQuery(
                // language=sql
                "DELETE FROM book"
        )
    }

    companion object {
        @Container
        private val postgresqlContainer: PostgreSQLContainer<*> = PostgreSQLContainer("postgres:13-alpine")

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            postgresqlContainer.start()
        }

        @JvmStatic
        @AfterAll
        fun afterAll() {
            postgresqlContainer.stop()
        }

        @JvmStatic
        @DynamicPropertySource
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgresqlContainer::getUsername)
            registry.add("spring.datasource.password", postgresqlContainer::getPassword)
        }
    }

    @Test
    fun `get all books from db`() {
        // GIVEN
        performQuery(
                // language=sql
                """
               insert into book (title, author)
               values 
                   ('Avatar', 'James'),
                   ('La ligne verte', 'Stephen'),
                   ('Les aristochats', 'Disney');
            """.trimIndent())

        // WHEN
        val res = bookDAO.list()

        // THEN
        assertThat(res).containsExactlyInAnyOrder(
                Book("Avatar", "James"),
                Book("La ligne verte", "Stephen"),
                Book("Les aristochats", "Disney")
        )
    }

    @Test
    fun `add a book in db`() {
        // GIVEN
        // WHEN
        bookDAO.createBook(Book("Avatar", "James"))

        // THEN
        val res = performQuery(
                // language=sql
                "SELECT * from book"
        )

        assertThat(res.size).isEqualTo(1)
        assertThat(res[0]["id"]).isNotNull()
        assertThat(res[0]["id"] is Int).isTrue()
        assertThat(res[0]["title"]).isEqualTo("Avatar")
        assertThat(res[0]["author"]).isEqualTo("James")
    }

    // TP
    @Test
    fun `get book by title in db`() {
        // GIVEN
        performQuery(
                // language=sql
                " insert into book (title, author) values('Avatar', 'James') "
        )

        // WHEN
        val book = bookDAO.getBookByTitle("Avatar")

        // THEN
        assertThat(book).isNotNull()
        assertThat(book?.title).isEqualTo("Avatar")
        assertThat(book?.author).isEqualTo("James")
        assertThat(book?.reserved).isEqualTo(false)
    }

    // TP
    @Test
    fun `reserve book in db`() {
        // GIVEN
        performQuery(
                // language=sql
                " insert into book (title, author) values('Avatar', 'James') "
        )

        // WHEN
        bookDAO.reserveBook("Avatar")

        // THEN
        val res = performQuery(
                // language=sql
                "SELECT * from book where title = 'Avatar'"
        )

        assertThat(res[0]["title"]).isEqualTo("Avatar")
        assertThat(res[0]["reserved"]).isEqualTo(true)
    }

    protected fun performQuery(sql: String): List<Map<String, Any>> {
        val hikariConfig = HikariConfig()
        hikariConfig.setJdbcUrl(postgresqlContainer.jdbcUrl)
        hikariConfig.username = postgresqlContainer.username
        hikariConfig.password = postgresqlContainer.password
        hikariConfig.setDriverClassName(postgresqlContainer.driverClassName)
        val ds = HikariDataSource(hikariConfig)

        val statement = ds.getConnection().createStatement()
        statement.execute(sql)
        val resultSet = statement.resultSet
        return resultSet?.toList() ?: listOf()
    }

    private fun ResultSet.toList(): List<Map<String, Any>> {
        val md = this.metaData
        val columns = md.columnCount
        val rows: MutableList<Map<String, Any>> = ArrayList()
        while (this.next()) {
            val row: MutableMap<String, Any> = HashMap(columns)
            for (i in 1..columns) {
                row[md.getColumnName(i)] = this.getObject(i)
            }
            rows.add(row)
        }
        return rows
    }
}
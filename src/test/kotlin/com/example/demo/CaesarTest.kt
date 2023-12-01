package com.example.demo

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.*
import net.jqwik.api.*
import net.jqwik.api.constraints.UpperChars
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

class CaesarTest {
    @Test
    fun `shift from A with 2 should be C`() {
        val char = 'A'
        val shiftNumber = 2
        val cypher = Cypher();

        val res = cypher.shift(char, shiftNumber)

        assertThat(res).isEqualTo('C')
    }

    @Test
    fun `shift from A with 5 should be F`() {
        val char = 'A'
        val shiftNumber = 5
        val cypher = Cypher();

        val res = cypher.shift(char, shiftNumber)

        assertThat(res).isEqualTo('F')
    }

    @Test
    fun `shift from Y with 2 should be A`() {
        val char = 'Y'
        val shiftNumber = 2
        val cypher = Cypher();

        val res = cypher.shift(char, shiftNumber)

        assertThat(res).isEqualTo('A')
    }

    @Test
    fun `shift from B with 53 should be C`() {
        val char = 'B'
        val shiftNumber = 53
        val cypher = Cypher();

        val res = cypher.shift(char, shiftNumber)

        assertThat(res).isEqualTo('C')
    }

    @Test
    fun `shift number lower than 0 throw an error`() {
        val char = 'A'
        val shiftNumber = -3
        val cypher = Cypher();

        assertFailure{
            cypher.shift(char, shiftNumber)
        }.hasMessage("shiftNumber should be superior or equal to 0")
    }

    @Test
    fun `unshift from C with 2 should be A`() {
        val char = 'C'
        val unshiftNumber = 2
        val cypher = Cypher();

        val res = cypher.unshift(char, unshiftNumber)

        assertThat(res).isEqualTo('A')
    }

    @Test
    fun `unshift from F with 5 should be A`() {
        val char = 'F'
        val shiftNumber = 5
        val cypher = Cypher();

        val res = cypher.unshift(char, shiftNumber)

        assertThat(res).isEqualTo('A')
    }

    @Test
    fun `unshift from A with 2 should be Y`() {
        val char = 'A'
        val shiftNumber = 2
        val cypher = Cypher();

        val res = cypher.unshift(char, shiftNumber)

        assertThat(res).isEqualTo('Y')
    }

    @Test
    fun `unshift from C with 53 should be B`() {
        val char = 'C'
        val shiftNumber = 53
        val cypher = Cypher();

        val res = cypher.unshift(char, shiftNumber)

        assertThat(res).isEqualTo('B')
    }

    @Test
    fun `unshift number lower than 0 throw an error`() {
        val char = 'A'
        val shiftNumber = -3
        val cypher = Cypher();

        assertFailure{
            cypher.unshift(char, shiftNumber)
        }.hasMessage("shiftNumber should be superior or equal to 0")
    }

    @Property
    fun `unshift resulted char is shift based char`(
            @ForAll("positifInt") shiftNumber: Int,
            @ForAll("correctChar") char: Char) {
        val cypher = Cypher();

        val res = cypher.shift(char, shiftNumber)
        assertThat(cypher.unshift(res, shiftNumber)).isEqualTo(char)
    }

    @Property
    fun `shift resulted char is unshift based char`(
            @ForAll("positifInt") shiftNumber: Int,
            @ForAll("correctChar") char: Char) {
        val cypher = Cypher();

        val res = cypher.unshift(char, shiftNumber)
        assertThat(cypher.shift(res, shiftNumber)).isEqualTo(char)
    }

    @Property
    fun `shift resulted char is correct char`(
            @ForAll("positifInt") shiftNumber: Int,
            @ForAll("correctChar") char: Char) {
        val cypher = Cypher();

        val res = cypher.shift(char, shiftNumber)
        assertThat(res).isBetween('A', 'Z')
    }

    @Provide
    fun positifInt(): Arbitrary<Int> {
        return Arbitraries.integers().greaterOrEqual(0)
    }

    @Provide
    fun correctChar(): Arbitrary<Char> {
        return Arbitraries.chars().range('A', 'Z')
    }
}
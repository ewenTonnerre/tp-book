package com.example.demo

class Cypher {
    fun shift(char: Char, shiftNumber: Int) : Char {
        if(shiftNumber < 0) throw Exception("shiftNumber should be superior or equal to 0")
        var shiftNumberFinal = shiftNumber
        if(shiftNumber > 26) shiftNumberFinal = shiftNumber % 26

        val res : Char = char.plus(shiftNumberFinal)

        if(res > 'Z'){
            return res.minus(26)
        }
        return res
    }

    fun unshift(char: Char, unshiftNumber: Int): Char {
        if(unshiftNumber < 0) throw Exception("shiftNumber should be superior or equal to 0")
        var unshiftNumberFinal = unshiftNumber
        if(unshiftNumber > 26) unshiftNumberFinal = unshiftNumber % 26

        val res : Char = char.minus(unshiftNumberFinal)

        if(res < 'A'){
            return res.plus(26)
        }
        return res
    }
}
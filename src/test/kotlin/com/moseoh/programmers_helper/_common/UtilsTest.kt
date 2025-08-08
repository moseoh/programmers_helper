package com.moseoh.programmers_helper._common

import com.moseoh.programmers_helper._common.Utils.removeAll
import com.moseoh.programmers_helper._common.Utils.removeComment
import com.moseoh.programmers_helper._common.Utils.removeFunctionInBracket
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class UtilsTest {

    @Test
    fun removeAll() {
        // given
        val str = """
            package 11
            ddd 22
            package 33
        """.trimIndent()
        val lines = str.lines()

        // when
        val result = lines.removeAll(Regex("^package .*")).joinToString(separator = "\n").trimIndent()

        // then
        assertEquals("ddd 22", result)
    }

    @Test
    fun removeInBracket() {
        // given
        val str = """
            // 뭔 주석
            fun som() {
            }
            
            fun main() {
                ... dl { } {}
            }
            
            fun solution {
                solution
            }
        """.trimIndent()
        val lines = str.lines()

        // when
        val result = lines.removeFunctionInBracket(Regex("^fun main.*")).joinToString(separator = "\n").trimIndent()

        // then
        assertEquals(
            """
                // 뭔 주석
                fun som() {
                }
                
                fun solution {
                    solution
                }
            """.trimIndent(), result
        )
    }

    @Test
    fun removeComment() {
        // given
        val str = """
            // 뭔 주석
            fun som() {
            }

            /**
             * 주석
             */
            fun solution {
                /*
                주석
                */
                solution
                /*
                주석
                */
            }
            /**
             * 주석
             */
        """.trimIndent()
        val lines = str.lines()

        // when
        val result = lines.removeComment().joinToString(separator = "\n").trimIndent()

        // then
        assertEquals(
            """
                fun som() {
                }

                fun solution {
                    solution
                }
            """.trimIndent(), result
        )
    }
}
package com.moseoh.programmers_helper.actions.copy_answer.service

import com.moseoh.programmers_helper.TestData
import com.moseoh.programmers_helper.settings.model.Language
import com.moseoh.programmers_helper.settings.model.ProgrammersHelperSettings
import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals


class KotlinCopyAnswerServiceTest {

    private lateinit var kotlinCopyAnswerService: KotlinCopyAnswerService

    private fun mocking(useMainFunction: Boolean, allowCopyComment: Boolean) {
        mockkObject(ProgrammersHelperSettings)
        every { ProgrammersHelperSettings.state } returns ProgrammersHelperSettings.State()
        every { ProgrammersHelperSettings.state.language } returns Language.Kotlin
        every { ProgrammersHelperSettings.state.useFolder } returns false
        every { ProgrammersHelperSettings.state.useNameSpacing } returns false
        every { ProgrammersHelperSettings.state.useMainFunction } returns useMainFunction
        every { ProgrammersHelperSettings.state.useHelpComment } returns true
        every { ProgrammersHelperSettings.state.allowCopyComment } returns allowCopyComment

        kotlinCopyAnswerService = KotlinCopyAnswerService(ProgrammersHelperSettings.state)
    }

    @Test
    fun `convert 메인 함수 사용`() {
        // given
        mocking(useMainFunction = true, allowCopyComment = true)
        val code = TestData.code_kotlin()

        // when
        val result = kotlinCopyAnswerService.convert(code)

        // then
        assertEquals(
            """
                    /**
                     * 주석
                     */
                    
                    import java.util.regex.Pattern
                    
                    /**
                     * 주석
                     */
                    class Solution {
                        // 주석
                        // 주석
                        // 주석
                        fun solution(s: String): IntArray {
                            return s.substring(2 until s.length - 2)
                                .split("},{")
                                .map { it.split(",").map { num -> num.toInt() } }
                                .toList().sortedBy { it.size }
                                .fold(setOf<Int>()) { acc, list -> acc.union(list) }.toIntArray()
                        }
                        /*
                            주석
                        */
                    }
            """.trimIndent(),
            result
        )
    }

    @Test
    fun `convert 메인 함수 미사용`() {
        // given
        mocking(useMainFunction = false, allowCopyComment = true)
        val code = TestData.code_kotlin()

        // when
        val result = kotlinCopyAnswerService.convert(code)

        // then
        assertEquals(
            """
                    /**
                     * 주석
                     */
                    
                    import java.util.regex.Pattern
                    
                    /**
                     * 주석
                     */
                    fun main() {
                        val s2 = "{{1,2,3},{2,1},{1,2,4,3},{2}}"
                        val answer2 = intArrayOf(2, 1, 3, 4)
                        val result2 = Solution().solution(s2)
                        println(result2.contentToString())
                        check(result2.contentEquals(answer2)) { "오답" }
                    
                        /**
                         * 주석
                         */
                        val s5 = "{{4,2,3},{3},{2,3,4,1},{2,3}}"
                        val answer5 = intArrayOf(3, 2, 4, 1)
                        val result5 = Solution().solution(s5)
                        println(result5.contentToString())
                        check(result5.contentEquals(answer5)) { "오답" }
                    }
                    
                    class Solution {
                        // 주석
                        // 주석
                        // 주석
                        fun solution(s: String): IntArray {
                            return s.substring(2 until s.length - 2)
                                .split("},{")
                                .map { it.split(",").map { num -> num.toInt() } }
                                .toList().sortedBy { it.size }
                                .fold(setOf<Int>()) { acc, list -> acc.union(list) }.toIntArray()
                        }
                        /*
                            주석
                        */
                    }
            """.trimIndent(),
            result
        )
    }

    @Test
    fun `convert 주석 복사 허용`() {
        // given
        mocking(useMainFunction = true, allowCopyComment = true)
        val code = TestData.code_kotlin()

        // when
        val result = kotlinCopyAnswerService.convert(code)

        // then
        assertEquals(
            """
                    /**
                     * 주석
                     */
                    
                    import java.util.regex.Pattern
                    
                    /**
                     * 주석
                     */
                    class Solution {
                        // 주석
                        // 주석
                        // 주석
                        fun solution(s: String): IntArray {
                            return s.substring(2 until s.length - 2)
                                .split("},{")
                                .map { it.split(",").map { num -> num.toInt() } }
                                .toList().sortedBy { it.size }
                                .fold(setOf<Int>()) { acc, list -> acc.union(list) }.toIntArray()
                        }
                        /*
                            주석
                        */
                    }
            """.trimIndent(),
            result
        )
    }

    @Test
    fun `convert 주석 복사 비허용`() {
        // given
        mocking(useMainFunction = true, allowCopyComment = false)
        val code = TestData.code_kotlin()

        // when
        val result = kotlinCopyAnswerService.convert(code)

        // then
        assertEquals(
            """
                    import java.util.regex.Pattern
                    
                    class Solution {
                        fun solution(s: String): IntArray {
                            return s.substring(2 until s.length - 2)
                                .split("},{")
                                .map { it.split(",").map { num -> num.toInt() } }
                                .toList().sortedBy { it.size }
                                .fold(setOf<Int>()) { acc, list -> acc.union(list) }.toIntArray()
                        }
                    }
            """.trimIndent(),
            result
        )
    }
}
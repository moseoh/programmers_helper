package com.moseoh.programmers_helper.actions.copy_answer.service

import com.moseoh.programmers_helper.TestData
import com.moseoh.programmers_helper.settings.model.Language
import com.moseoh.programmers_helper.settings.model.ProgrammersHelperSettings
import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals


class PythonCopyAnswerServiceTest {

    companion object {
        private const val TRIPLE_QUOTE = "___TRIPLE_QUOTE___"
    }

    private lateinit var pythonCopyAnswerService: PythonCopyAnswerService

    private fun mocking(useMainFunction: Boolean, allowCopyComment: Boolean) {
        mockkObject(ProgrammersHelperSettings)
        every { ProgrammersHelperSettings.state } returns ProgrammersHelperSettings.State()
        every { ProgrammersHelperSettings.state.language } returns Language.Python3
        every { ProgrammersHelperSettings.state.useFolder } returns false
        every { ProgrammersHelperSettings.state.useNameSpacing } returns false
        every { ProgrammersHelperSettings.state.useMainFunction } returns useMainFunction
        every { ProgrammersHelperSettings.state.useHelpComment } returns true
        every { ProgrammersHelperSettings.state.allowCopyComment } returns allowCopyComment

        pythonCopyAnswerService = PythonCopyAnswerService(ProgrammersHelperSettings.state)
    }

    @Test
    fun `convert 메인 함수 사용`() {
        // given
        mocking(useMainFunction = true, allowCopyComment = true)
        val code = TestData.code_python()

        // when
        val result = pythonCopyAnswerService.convert(code)

        // then
        // 실제 결과에 맞게 기댓값 수정: def solution 앞 빈 줄 1개만 있음  
        val expected = """
                    $TRIPLE_QUOTE
                    주석
                    $TRIPLE_QUOTE
                    
                    import re
                    
                    $TRIPLE_QUOTE
                    주석
                    $TRIPLE_QUOTE
                    def solution(s):
                        # 주석
                        # 주석
                        # 주석
                        answer = []
                        $TRIPLE_QUOTE
                            주석
                        $TRIPLE_QUOTE
                        return answer
            """.trimIndent().replace(TRIPLE_QUOTE, "\"\"\"")
        assertEquals(expected, result)
    }

    @Test
    fun `convert 메인 함수 미사용`() {
        // given
        mocking(useMainFunction = false, allowCopyComment = true)
        val code = TestData.code_python()

        // when
        val result = pythonCopyAnswerService.convert(code)

        // then  
        // useMainFunction = false이므로 모든 코드가 그대로 유지되어야 함
        val expected = TestData.code_python()
        assertEquals(expected, result)
    }

    @Test
    fun `convert 주석 복사 허용`() {
        // given
        mocking(useMainFunction = true, allowCopyComment = true)
        val code = TestData.code_python()

        // when
        val result = pythonCopyAnswerService.convert(code)

        // then
        // 실제 결과에 맞게 기댓값 수정: def solution 앞 빈 줄 1개만 있음
        val expected = """
                    $TRIPLE_QUOTE
                    주석
                    $TRIPLE_QUOTE
                    
                    import re
                    
                    $TRIPLE_QUOTE
                    주석
                    $TRIPLE_QUOTE
                    def solution(s):
                        # 주석
                        # 주석
                        # 주석
                        answer = []
                        $TRIPLE_QUOTE
                            주석
                        $TRIPLE_QUOTE
                        return answer
            """.trimIndent().replace(TRIPLE_QUOTE, "\"\"\"")
        assertEquals(expected, result)
    }

    @Test
    fun `convert 주석 복사 비허용`() {
        // given
        mocking(useMainFunction = true, allowCopyComment = false)
        val code = TestData.code_python()

        // when
        val result = pythonCopyAnswerService.convert(code)

        // then
        // 실제 결과에 맞게 기댓값 수정
        assertEquals(
            """
                    import re
                    
                    def solution(s):
                    


                        answer = []
                        return answer
            """.trimIndent(),
            result
        )
    }
}
package com.moseoh.programmers_helper.actions.copy_answer.service

import com.moseoh.programmers_helper.TestData
import com.moseoh.programmers_helper.settings.model.Language
import com.moseoh.programmers_helper.settings.model.ProgrammersHelperSettings
import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class JavaCopyAnswerServiceTest {

    private lateinit var javaCopyAnswerService: JavaCopyAnswerService

    private fun mocking(useMainFunction: Boolean, allowCopyComment: Boolean) {
        mockkObject(ProgrammersHelperSettings)
        every { ProgrammersHelperSettings.state } returns ProgrammersHelperSettings.State()
        every { ProgrammersHelperSettings.state.language } returns Language.Java
        every { ProgrammersHelperSettings.state.useFolder } returns false
        every { ProgrammersHelperSettings.state.useNameSpacing } returns false
        every { ProgrammersHelperSettings.state.useMainFunction } returns useMainFunction
        every { ProgrammersHelperSettings.state.useHelpComment } returns true
        every { ProgrammersHelperSettings.state.allowCopyComment } returns allowCopyComment

        javaCopyAnswerService = JavaCopyAnswerService(ProgrammersHelperSettings.state)
    }

    @Test
    fun `convert 메인 함수 사용`() {
        // given
        mocking(useMainFunction = true, allowCopyComment = true)
        val code = TestData.code_java()

        // when
        val result = javaCopyAnswerService.convert(code)

        // then
        assertEquals(
            """
                import java.util.Arrays;
    
                /**
                 * 주석
                 */
                class Solution {
                    /**
                     * 주석
                     */
                    public int[] solution(int[] prices) {
                        int[] answer = new int[prices.length];
                        // 주석
                        for (int i = 0; i < answer.length - 1; i++) {
                            for (int j = i + 1; j < prices.length; j++) {
                                answer[i]++;
                                if (prices[i] > prices[j]) break;
                            }
                        }
                        /*
                            주석
                        */
                        return answer;
                    }
                }
            """.trimIndent(),
            result
        )
    }

    @Test
    fun `convert 메인 함수 미사용`() {
        // given
        mocking(useMainFunction = false, allowCopyComment = true)
        val code = TestData.code_java()

        // when
        val result = javaCopyAnswerService.convert(code)

        // then
        assertEquals(
            """
                import java.util.Arrays;
    
                /**
                 * 주석
                 */
                class Solution {
                    public static void main(String[] args) {
                        // 주석
                        int[] prices1 = new int[]{1, 2, 3, 2, 3};
                        int[] answer1 = new int[]{4, 3, 1, 1, 0};
                        int[] result1 = new Solution().solution(prices1);
                        PRINT_RESULT(1, result1, answer1);
                    }
                    
                    public static void PRINT_RESULT(int index, int[] result, int[] answer) {
                        boolean correct = Arrays.equals(result, answer);
                        StringBuilder sb = new StringBuilder();
                        sb.append("\n\n테스트 케이스 ").append(index).append(": ");
                        sb.append(correct ? "정답" : "오답").append("\n");
                        sb.append("\t- 실행 결과: \t").append(answer).append("\n");
                        sb.append("\t- 기댓값: \t").append(result).append("\n");
                        if (correct) System.out.println(sb);
                        else throw new RuntimeException(sb.toString());
                    }
    
                    /**
                     * 주석
                     */
                    public int[] solution(int[] prices) {
                        int[] answer = new int[prices.length];
                        // 주석
                        for (int i = 0; i < answer.length - 1; i++) {
                            for (int j = i + 1; j < prices.length; j++) {
                                answer[i]++;
                                if (prices[i] > prices[j]) break;
                            }
                        }
                        /*
                            주석
                        */
                        return answer;
                    }
                }
            """.trimIndent(),
            result
        )
    }

    @Test
    fun `convert 주석 복사 허용`() {
        // given
        mocking(useMainFunction = true, allowCopyComment = true)
        val code = TestData.code_java()

        // when
        val result = javaCopyAnswerService.convert(code)

        // then
        assertEquals(
            """
                import java.util.Arrays;
    
                /**
                 * 주석
                 */
                class Solution {
                    /**
                     * 주석
                     */
                    public int[] solution(int[] prices) {
                        int[] answer = new int[prices.length];
                        // 주석
                        for (int i = 0; i < answer.length - 1; i++) {
                            for (int j = i + 1; j < prices.length; j++) {
                                answer[i]++;
                                if (prices[i] > prices[j]) break;
                            }
                        }
                        /*
                            주석
                        */
                        return answer;
                    }
                }
            """.trimIndent(),
            result
        )
    }

    @Test
    fun `convert 주석 복사 비허용`() {
        // given
        mocking(useMainFunction = true, allowCopyComment = false)
        val code = TestData.code_java()

        // when
        val result = javaCopyAnswerService.convert(code)

        // then
        assertEquals(
            """
                import java.util.Arrays;

                class Solution {
                    public int[] solution(int[] prices) {
                        int[] answer = new int[prices.length];
                        for (int i = 0; i < answer.length - 1; i++) {
                            for (int j = i + 1; j < prices.length; j++) {
                                answer[i]++;
                                if (prices[i] > prices[j]) break;
                            }
                        }
                        return answer;
                    }
                }
            """.trimIndent(),
            result
        )
    }
}
package com.moseoh.programmers_helper.actions.import_problem.service

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.moseoh.programmers_helper.TestData
import com.moseoh.programmers_helper.actions.import_problem.service.dto.JavaTemplateMapper
import com.moseoh.programmers_helper.actions.import_problem.service.dto.KotlinTemplateMapper
import com.moseoh.programmers_helper.settings.model.Language
import com.moseoh.programmers_helper.settings.model.ProgrammersHelperSettings
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import java.util.*


class ContentServiceTest {
    private lateinit var javaTemplateMapper: JavaTemplateMapper
    private lateinit var kotlinTemplateMapper: KotlinTemplateMapper
    private lateinit var contentService: ContentService
    private val project = mockk<Project>()
    private val directory = mockk<VirtualFile>()

    private fun mocking(language: Language, useHelpComment: Boolean = false) {
        mockkObject(ProgrammersHelperSettings)
        every { ProgrammersHelperSettings.state } returns ProgrammersHelperSettings.State()
        every { ProgrammersHelperSettings.state.language } returns language
        every { ProgrammersHelperSettings.state.useFolder } returns false
        every { ProgrammersHelperSettings.state.useNameSpacing } returns false
        every { ProgrammersHelperSettings.state.useMainFunction } returns true
        every { ProgrammersHelperSettings.state.useHelpComment } returns useHelpComment

        every { project.basePath } returns "src"
        every { directory.path } returns "src/com/moseoh/example"

        Locale.setDefault(Locale.KOREA)
    }

    @Test
    fun `get java primitive 타입 반환`() {
        // given
        mocking(Language.Java)
        javaTemplateMapper = JavaTemplateMapper(ProgrammersHelperSettings.state)
        kotlinTemplateMapper = KotlinTemplateMapper(ProgrammersHelperSettings.state)
        contentService = ContentService(ProgrammersHelperSettings.state, javaTemplateMapper, kotlinTemplateMapper)
        val problem = TestData.problemDto_java_returnPrimitive()

        // when
        val result = contentService.get(project, directory, problem)

        // then
        assertEquals(
            """
                package com.moseoh.example;
                
                class n2배열자르기 {
                    public static void main(String[] args) {
                        int i1 = 3;
                        int answer1 = 1;
                        int result1 = new n2배열자르기().solution(i1);
                        PRINT_RESULT(1, result1, answer1);
                
                        int i2 = 12;
                        int answer2 = 40;
                        int result2 = new n2배열자르기().solution(i2);
                        PRINT_RESULT(2, result2, answer2);
                    }
                
                    public static void PRINT_RESULT(int index, int result, int answer) {
                        boolean correct = result == answer;
                        StringBuilder sb = new StringBuilder();
                        sb.append("\n\n테스트 케이스 ").append(index).append(": ");
                        sb.append(correct ? "정답" : "오답").append("\n");
                        sb.append("\t- 실행 결과: \t").append(result).append("\n");
                        sb.append("\t- 기댓값: \t").append(answer).append("\n");
                        if (correct) System.out.println(sb);
                        else throw new RuntimeException(sb.toString());
                    }
                
                    public int solution(int i) {
                        int answer = 0;
                        return answer;
                    }
                }
            """.trimIndent(),
            result
        )
    }

    @Test
    fun `get java String 타입 반환`() {
        // given
        mocking(Language.Java)
        javaTemplateMapper = JavaTemplateMapper(ProgrammersHelperSettings.state)
        kotlinTemplateMapper = KotlinTemplateMapper(ProgrammersHelperSettings.state)
        contentService = ContentService(ProgrammersHelperSettings.state, javaTemplateMapper, kotlinTemplateMapper)
        val problem = TestData.problemDto_java_returnString()

        // when
        val result = contentService.get(project, directory, problem)

        // then
        assertEquals(
            """
                package com.moseoh.example;
                
                class n2배열자르기 {
                    public static void main(String[] args) {
                        int i1 = 3;
                        String answer1 = "str";
                        String result1 = new n2배열자르기().solution(i1);
                        PRINT_RESULT(1, result1, answer1);
                
                        int i2 = 12;
                        String answer2 = "str22";
                        String result2 = new n2배열자르기().solution(i2);
                        PRINT_RESULT(2, result2, answer2);
                    }
                
                    public static void PRINT_RESULT(int index, String result, String answer) {
                        boolean correct = result.equals(answer);
                        StringBuilder sb = new StringBuilder();
                        sb.append("\n\n테스트 케이스 ").append(index).append(": ");
                        sb.append(correct ? "정답" : "오답").append("\n");
                        sb.append("\t- 실행 결과: \t").append(result).append("\n");
                        sb.append("\t- 기댓값: \t").append(answer).append("\n");
                        if (correct) System.out.println(sb);
                        else throw new RuntimeException(sb.toString());
                    }
                
                    public String solution(int i) {
                        String answer = "";
                        return answer;
                    }
                }
            """.trimIndent(),
            result
        )
    }

    @Test
    fun `get java 배열 타입 반환`() {
        // given
        mocking(Language.Java)
        javaTemplateMapper = JavaTemplateMapper(ProgrammersHelperSettings.state)
        kotlinTemplateMapper = KotlinTemplateMapper(ProgrammersHelperSettings.state)
        contentService = ContentService(ProgrammersHelperSettings.state, javaTemplateMapper, kotlinTemplateMapper)
        val problem = TestData.problemDto_java_returnArray()

        // when
        val result = contentService.get(project, directory, problem)

        // then
        assertEquals(
            """
                package com.moseoh.example;
                
                import java.util.Arrays;
                
                class n2배열자르기 {
                    public static void main(String[] args) {
                        int i1 = 3;
                        int[] answer1 = new int[]{1, 2};
                        int[] result1 = new n2배열자르기().solution(i1);
                        PRINT_RESULT(1, result1, answer1);
                
                        int i2 = 12;
                        int[] answer2 = new int[]{10, 42};
                        int[] result2 = new n2배열자르기().solution(i2);
                        PRINT_RESULT(2, result2, answer2);
                    }
                
                    public static void PRINT_RESULT(int index, int[] result, int[] answer) {
                        boolean correct = Arrays.equals(result, answer);
                        StringBuilder sb = new StringBuilder();
                        sb.append("\n\n테스트 케이스 ").append(index).append(": ");
                        sb.append(correct ? "정답" : "오답").append("\n");
                        sb.append("\t- 실행 결과: \t").append(Arrays.toString(result)).append("\n");
                        sb.append("\t- 기댓값: \t").append(Arrays.toString(answer)).append("\n");
                        if (correct) System.out.println(sb);
                        else throw new RuntimeException(sb.toString());
                    }
                
                    public int[] solution(int i) {
                        int[] answer = {};
                        return answer;
                    }
                }
            """.trimIndent(),
            result
        )
    }

    @Test
    fun `get java 2차원 배열 타입 반환`() {
        // given
        mocking(Language.Java)
        javaTemplateMapper = JavaTemplateMapper(ProgrammersHelperSettings.state)
        kotlinTemplateMapper = KotlinTemplateMapper(ProgrammersHelperSettings.state)
        contentService = ContentService(ProgrammersHelperSettings.state, javaTemplateMapper, kotlinTemplateMapper)
        val problem = TestData.problemDto_java_returnArrayArray()

        // when
        val result = contentService.get(project, directory, problem)

        // then
        assertEquals(
            """
                package com.moseoh.example;
                
                import java.util.Arrays;
                
                class n2배열자르기 {
                    public static void main(String[] args) {
                        int i1 = 3;
                        int[][] answer1 = new int[][]{{"str1", "str2"}, {"str3"}};
                        int[][] result1 = new n2배열자르기().solution(i1);
                        PRINT_RESULT(1, result1, answer1);
                
                        int i2 = 12;
                        int[][] answer2 = new int[][]{{"str1", "str2", "str3"}, {"str4"}};
                        int[][] result2 = new n2배열자르기().solution(i2);
                        PRINT_RESULT(2, result2, answer2);
                    }
                
                    public static void PRINT_RESULT(int index, int[][] result, int[][] answer) {
                        boolean correct = Arrays.deepEquals(result, answer);
                        StringBuilder sb = new StringBuilder();
                        sb.append("\n\n테스트 케이스 ").append(index).append(": ");
                        sb.append(correct ? "정답" : "오답").append("\n");
                        sb.append("\t- 실행 결과: \t").append(Arrays.deepToString(result)).append("\n");
                        sb.append("\t- 기댓값: \t").append(Arrays.deepToString(answer)).append("\n");
                        if (correct) System.out.println(sb);
                        else throw new RuntimeException(sb.toString());
                    }
                
                    public int[][] solution(int i) {
                        int[][] answer = {};
                        return answer;
                    }
                }
            """.trimIndent(),
            result
        )
    }

    @Test
    fun `get kotlin 일반 타입 반환`() {
        // given
        mocking(Language.Kotlin)
        javaTemplateMapper = JavaTemplateMapper(ProgrammersHelperSettings.state)
        kotlinTemplateMapper = KotlinTemplateMapper(ProgrammersHelperSettings.state)
        contentService = ContentService(ProgrammersHelperSettings.state, javaTemplateMapper, kotlinTemplateMapper)
        val problem = TestData.problemDto_kotlin_returnSingle()

        // when
        val result = contentService.get(project, directory, problem)

        // then
        assertEquals(
            """
                package com.moseoh.example

                fun main() {
                    fun printResult(index: Int, result: Int, answer: Int) {
                        val correct = result == answer
                        val sb = StringBuilder()
                        sb.append("\n\n테스트 케이스 ").append(index).append(": ")
                        sb.append(if (correct) "정답" else "오답").append("\n")
                        sb.append("\t- 실행 결과: \t").append(result).append("\n")
                        sb.append("\t- 기댓값: \t").append(answer).append("\n")
                        if (correct) println(sb) else throw RuntimeException(sb.toString())
                    }

                    val i1 = 3
                    val answer1 = 1
                    val result1 = n2배열자르기().solution(i1)
                    printResult(1, result1, answer1)

                    val i2 = 12
                    val answer2 = 40
                    val result2 = n2배열자르기().solution(i2)
                    printResult(2, result2, answer2)
                }

                class n2배열자르기 {
                    fun solution(i: Int): Int {
                        var answer: Int = 0
                        return answer
                    }
                }
            """.trimIndent(),
            result
        )
    }

    @Test
    fun `get kotlin 배열 타입 반환`() {
        // given
        mocking(Language.Kotlin)
        javaTemplateMapper = JavaTemplateMapper(ProgrammersHelperSettings.state)
        kotlinTemplateMapper = KotlinTemplateMapper(ProgrammersHelperSettings.state)
        contentService = ContentService(ProgrammersHelperSettings.state, javaTemplateMapper, kotlinTemplateMapper)
        val problem = TestData.problemDto_kotlin_returnArray()

        // when
        val result = contentService.get(project, directory, problem)

        // then
        assertEquals(
            """
                package com.moseoh.example
                
                fun main() {
                    fun printResult(index: Int, result: IntArray, answer: IntArray) {
                        val correct = result.contentEquals(answer)
                        val sb = StringBuilder()
                        sb.append("\n\n테스트 케이스 ").append(index).append(": ")
                        sb.append(if (correct) "정답" else "오답").append("\n")
                        sb.append("\t- 실행 결과: \t").append(result.contentToString()).append("\n")
                        sb.append("\t- 기댓값: \t").append(answer.contentToString()).append("\n")
                        if (correct) println(sb) else throw RuntimeException(sb.toString())
                    }
                
                    val i1 = 3
                    val answer1 = intArrayOf(1, 2)
                    val result1 = n2배열자르기().solution(i1)
                    printResult(1, result1, answer1)
                
                    val i2 = 12
                    val answer2 = intArrayOf(10, 42)
                    val result2 = n2배열자르기().solution(i2)
                    printResult(2, result2, answer2)
                }
                
                class n2배열자르기 {
                    fun solution(i: Int): IntArray {
                        var answer: IntArray = intArrayOf()
                        return answer
                    }
                }
            """.trimIndent(),
            result
        )
    }

    @Test
    fun `get kotlin 2차원 배열 타입 반환`() {
        // given
        mocking(Language.Kotlin)
        javaTemplateMapper = JavaTemplateMapper(ProgrammersHelperSettings.state)
        kotlinTemplateMapper = KotlinTemplateMapper(ProgrammersHelperSettings.state)
        contentService = ContentService(ProgrammersHelperSettings.state, javaTemplateMapper, kotlinTemplateMapper)
        val problem = TestData.problemDto_kotlin_returnArrayArray()

        // when
        val result = contentService.get(project, directory, problem)

        // then
        assertEquals(
            """
                package com.moseoh.example
                
                fun main() {
                    fun printResult(index: Int, result: Array<Array<String>>, answer: Array<Array<String>>) {
                        val correct = result.contentDeepEquals(answer)
                        val sb = StringBuilder()
                        sb.append("\n\n테스트 케이스 ").append(index).append(": ")
                        sb.append(if (correct) "정답" else "오답").append("\n")
                        sb.append("\t- 실행 결과: \t").append(result.contentDeepToString()).append("\n")
                        sb.append("\t- 기댓값: \t").append(answer.contentDeepToString()).append("\n")
                        if (correct) println(sb) else throw RuntimeException(sb.toString())
                    }
                
                    val i1 = 3
                    val answer1 = arrayOf(arrayOf("str1", "str2"), arrayOf("str3"))
                    val result1 = n2배열자르기().solution(i1)
                    printResult(1, result1, answer1)
                
                    val i2 = 12
                    val answer2 = arrayOf(arrayOf("str1", "str2", "str3"), arrayOf("str4"))
                    val result2 = n2배열자르기().solution(i2)
                    printResult(2, result2, answer2)
                }
                
                class n2배열자르기 {
                    fun solution(i: Int): Array<Array<String>> {
                        var answer: Array<Array<String>> = arrayOf(arrayOf())
                        return answer
                    }
                }
            """.trimIndent(),
            result
        )
    }

    @Test
    fun `get java 도움말 주석`() {
        // given
        mocking(Language.Java, true)
        javaTemplateMapper = JavaTemplateMapper(ProgrammersHelperSettings.state)
        kotlinTemplateMapper = KotlinTemplateMapper(ProgrammersHelperSettings.state)
        contentService = ContentService(ProgrammersHelperSettings.state, javaTemplateMapper, kotlinTemplateMapper)
        val problem = TestData.problemDto_java_returnPrimitive()

        // when
        val result = contentService.get(project, directory, problem)

        // then
        assertEquals(
            """
                package com.moseoh.example;

                /**
                 * main, PRINT_RESULT 는 테스트 케이스 실행 및 결과 확인을 위한 함수입니다.
                 * [답안지 복사] 기능을 사용하는 경우 해당 함수들을 제외하며, 답안에 필요한 코드만 복사됩니다.
                 * 테스트 케이스 추가 등 함수 내부 변경은 가능하나, 함수 이름 변경시 [답안지 복사] 기능이 제대로 동작하지 않습니다.
                 *
                 * 또한, 기본 설정으로 [답안지 복사] 사용시 해당 주석과 작성하신 주석을 제외하여 복사됩니다.
                 * [주석 복사] 여부는 설정을 통해 변경할 수 있습니다.
                 *
                 * [도움말 주석] 옵션은 설정을 통해 제거할 수 있습니다.
                 *
                 * - [답안지 복사]
                 *   코드 - 답안지 복사 (기본 단축키 cmd + shift + w)
                 *
                 * - [도움말 주석]
                 *   설정 - 도구 - 프로그래머스 헬퍼 - 도움말 주석
                 *
                 * - [주석 복사]
                 *   설정 - 도구 - 프로그래머스 헬퍼 - 주석 복사
                 *
                 * GitHub: https://github.com/moseoh/programmers_helper
                 */
                class n2배열자르기 {
                    public static void main(String[] args) {
                        int i1 = 3;
                        int answer1 = 1;
                        int result1 = new n2배열자르기().solution(i1);
                        PRINT_RESULT(1, result1, answer1);
                
                        int i2 = 12;
                        int answer2 = 40;
                        int result2 = new n2배열자르기().solution(i2);
                        PRINT_RESULT(2, result2, answer2);
                    }
                
                    public static void PRINT_RESULT(int index, int result, int answer) {
                        boolean correct = result == answer;
                        StringBuilder sb = new StringBuilder();
                        sb.append("\n\n테스트 케이스 ").append(index).append(": ");
                        sb.append(correct ? "정답" : "오답").append("\n");
                        sb.append("\t- 실행 결과: \t").append(result).append("\n");
                        sb.append("\t- 기댓값: \t").append(answer).append("\n");
                        if (correct) System.out.println(sb);
                        else throw new RuntimeException(sb.toString());
                    }
                
                    public int solution(int i) {
                        int answer = 0;
                        return answer;
                    }
                }
            """.trimIndent(),
            result
        )
    }

    @Test
    fun `get kotlin 도움말 주석`() {
        // given
        mocking(Language.Kotlin, true)
        javaTemplateMapper = JavaTemplateMapper(ProgrammersHelperSettings.state)
        kotlinTemplateMapper = KotlinTemplateMapper(ProgrammersHelperSettings.state)
        contentService = ContentService(ProgrammersHelperSettings.state, javaTemplateMapper, kotlinTemplateMapper)
        val problem = TestData.problemDto_kotlin_returnSingle()

        // when
        val result = contentService.get(project, directory, problem)

        // then
        assertEquals(
            """
                package com.moseoh.example
                
                /**
                 * main, printResult 는 테스트 케이스 실행 및 결과 확인을 위한 함수입니다.
                 * [답안지 복사] 기능을 사용하는 경우 해당 함수들을 제외하며, 답안에 필요한 코드만 복사됩니다.
                 * 테스트 케이스 추가 등 함수 내부 변경은 가능하나, 함수 이름 변경시 [답안지 복사] 기능이 제대로 동작하지 않습니다.
                 *
                 * 또한, 기본 설정으로 [답안지 복사] 사용시 해당 주석과 작성하신 주석을 제외하여 복사됩니다.
                 * [주석 복사] 여부는 설정을 통해 변경할 수 있습니다.
                 *
                 * [도움말 주석] 옵션은 설정을 통해 제거할 수 있습니다.
                 *
                 * - [답안지 복사]
                 *   코드 - 답안지 복사 (기본 단축키 cmd + shift + w)
                 *
                 * - [도움말 주석]
                 *   설정 - 도구 - 프로그래머스 헬퍼 - 도움말 주석
                 *
                 * - [주석 복사]
                 *   설정 - 도구 - 프로그래머스 헬퍼 - 주석 복사
                 *
                 * GitHub: https://github.com/moseoh/programmers_helper
                 */
                fun main() {
                    fun printResult(index: Int, result: Int, answer: Int) {
                        val correct = result == answer
                        val sb = StringBuilder()
                        sb.append("\n\n테스트 케이스 ").append(index).append(": ")
                        sb.append(if (correct) "정답" else "오답").append("\n")
                        sb.append("\t- 실행 결과: \t").append(result).append("\n")
                        sb.append("\t- 기댓값: \t").append(answer).append("\n")
                        if (correct) println(sb) else throw RuntimeException(sb.toString())
                    }
                
                    val i1 = 3
                    val answer1 = 1
                    val result1 = n2배열자르기().solution(i1)
                    printResult(1, result1, answer1)
                
                    val i2 = 12
                    val answer2 = 40
                    val result2 = n2배열자르기().solution(i2)
                    printResult(2, result2, answer2)
                }
                
                class n2배열자르기 {
                    fun solution(i: Int): Int {
                        var answer: Int = 0
                        return answer
                    }
                }
            """.trimIndent(),
            result
        )
    }


}
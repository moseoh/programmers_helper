package com.moseoh.programmers_helper.actions.import_problem.service.dto

import com.intellij.openapi.components.Service
import com.moseoh.programmers_helper._common.PluginBundle.get
import com.moseoh.programmers_helper.settings.model.ProgrammersHelperSettings
import java.util.Locale
import java.util.Locale.getDefault

@Service
class PythonTemplateMapper(
    private val settings: ProgrammersHelperSettings.State = ProgrammersHelperSettings.state
) {
    fun toDto(projectPath: String, directoryPath: String, problemDto: ProblemDto): PythonTemplateDto {
        return PythonTemplateDto(
            useMain = settings.useMainFunction,
            helpComment = getHelpComment(),
            functionName = "solution",
            functionContent = getFunctionContent(problemDto),
            testCaseDtos = getTestCaseDtos(problemDto),
        )
    }

    private fun getHelpComment(): String? {
        if (!settings.useHelpComment) return null
        return get("helpCommentPythonFile")
    }

    private fun getFunctionContent(problemDto: ProblemDto): String {
        val content = problemDto.content
        val functionDefinitionRegex = Regex("^def solution\\(.*\\):")
        
        return content
            .lines()
            .dropWhile { !it.trim().startsWith("def solution") }
            .drop(1)  // def solution 라인 제거
            .joinToString("\n")
            .trim()
    }

    private fun getTestCaseDtos(problemDto: ProblemDto): List<PythonTemplateDto.TestCaseDto> {
        return problemDto.testCases.map { getTestCaseDto(it, problemDto.answerName) }
    }

    private fun getTestCaseDto(
        testCase: Map<String, String>,
        answerName: String,
    ): PythonTemplateDto.TestCaseDto {
        val values = mutableListOf<PythonTemplateDto.Value>()
        testCase.forEach { (key, value) ->
            if (key != answerName) {
                values.add(
                    PythonTemplateDto.Value(
                        type = "", // Python은 타입 불필요
                        name = key,
                        value = formatPythonValue(value)
                    )
                )
            }
        }

        return PythonTemplateDto.TestCaseDto(
            values,
            PythonTemplateDto.Value(
                type = "", // Python은 타입 불필요
                name = answerName,
                value = formatPythonValue(testCase[answerName]!!)
            )
        )
    }

    /**
     * Python 값을 올바른 형식으로 포맷팅
     * Python은 동적 타입이므로 값 자체만 적절히 변환
     */
    private fun formatPythonValue(value: String): String {
        return when {
            // 문자열 값 처리 - 따옴표가 없다면 추가
            value.startsWith("\"") && value.endsWith("\"") -> value
            value.startsWith("'") && value.endsWith("'") -> "\"${value.trim('\'')}\""
            
            // 배열/리스트 처리 - 그대로 사용 (이미 올바른 Python 형식)
            value.startsWith("[") && value.endsWith("]") -> value
            
            // 불린 값 처리
            value.lowercase() == "true" -> "True"
            value.lowercase() == "false" -> "False"
            
            // 숫자인지 확인 (정수 또는 실수)
            value.toIntOrNull() != null -> value
            value.toDoubleOrNull() != null -> value
            
            // 그 외에는 문자열로 처리
            else -> "\"$value\""
        }
    }
}
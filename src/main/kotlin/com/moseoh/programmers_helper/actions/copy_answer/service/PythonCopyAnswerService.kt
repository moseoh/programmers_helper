package com.moseoh.programmers_helper.actions.copy_answer.service

import com.intellij.openapi.components.Service
import com.moseoh.programmers_helper._common.Utils.removeAll
import com.moseoh.programmers_helper._common.Utils.removeComment
import com.moseoh.programmers_helper._common.Utils.removeFunctionInBracket
import com.moseoh.programmers_helper.actions.copy_answer.service.impl.ICopyAnswerService
import com.moseoh.programmers_helper.settings.model.ProgrammersHelperSettings

@Service
class PythonCopyAnswerService(
    private val settings: ProgrammersHelperSettings.State = ProgrammersHelperSettings.state,
) : ICopyAnswerService {
    override fun convert(code: String): String {
        var result = code

        // main 실행 블록 제거
        if (settings.useMainFunction) {
            // if __name__ == "__main__": 블록 제거
            result = removeMainBlock(result)
            
            // print_result 함수 제거
            result = removePrintResultFunction(result)
        }

        // 주석 제거
        if (!settings.allowCopyComment) {
            result = removePythonComments(result)
        }

        return result.trim()
    }
    
    private fun removeMainBlock(code: String): String {
        val lines = code.lines().toMutableList()
        val mainIndex = lines.indexOfFirst { it.trim().startsWith("if __name__") }
        
        return if (mainIndex != -1) {
            lines.take(mainIndex).joinToString("\n")
        } else {
            code
        }
    }
    
    private fun removePrintResultFunction(code: String): String {
        val lines = code.lines()
        val result = mutableListOf<String>()
        var inPrintResult = false
        var indentLevel = -1
        
        for (line in lines) {
            if (line.trim().startsWith("def print_result") || line.trim().startsWith("def PRINT_RESULT")) {
                inPrintResult = true
                indentLevel = line.indexOfFirst { it != ' ' && it != '\t' }
                continue
            }
            
            if (inPrintResult) {
                val currentIndent = if (line.trim().isEmpty()) Int.MAX_VALUE else line.indexOfFirst { it != ' ' && it != '\t' }
                if (currentIndent <= indentLevel) {
                    inPrintResult = false
                    result.add(line)
                }
            } else {
                result.add(line)
            }
        }
        
        return result.joinToString("\n")
    }
    
    private fun removePythonComments(code: String): String {
        val lines = code.lines()
        val result = mutableListOf<String>()
        var inMultilineComment = false
        
        for (line in lines) {
            var processedLine = line
            
            // 멀티라인 문자열(주석) 처리
            if (line.trim().startsWith("\"\"\"")) {
                inMultilineComment = !inMultilineComment
                continue  // 멀티라인 주석 시작/끝 라인은 제거
            }
            
            // 멀티라인 주석 중이면 스킵
            if (inMultilineComment) {
                continue
            }
            
            // 한 줄 주석 제거 (#으로 시작)
            val commentIndex = processedLine.indexOf('#')
            if (commentIndex != -1) {
                // 문자열 내부의 #는 제외하는 간단한 로직
                var inString = false
                var stringChar = '\u0000'
                var realCommentIndex = -1
                
                for (i in processedLine.indices) {
                    val char = processedLine[i]
                    if (!inString && (char == '"' || char == '\'')) {
                        // """ 문자열은 별도 처리
                        if (i <= processedLine.length - 3 && processedLine.substring(i, i + 3) == "\"\"\"") {
                            // 이미 멀티라인 주석으로 처리됨
                            continue
                        }
                        inString = true
                        stringChar = char
                    } else if (inString && char == stringChar && (i == 0 || processedLine[i-1] != '\\')) {
                        inString = false
                    } else if (!inString && char == '#') {
                        realCommentIndex = i
                        break
                    }
                }
                
                if (realCommentIndex != -1) {
                    processedLine = processedLine.substring(0, realCommentIndex).trimEnd()
                }
            }
            
            // 빈 줄은 그대로 유지 (Java와 동일한 동작)
            result.add(processedLine)
        }
        
        return result.joinToString("\n")
    }
}
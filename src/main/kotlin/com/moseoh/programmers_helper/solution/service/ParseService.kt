package com.moseoh.programmers_helper.solution.service

import com.intellij.openapi.components.Service
import com.moseoh.programmers_helper.settings.model.Language
import com.moseoh.programmers_helper.settings.model.ProgrammersHelperSettings
import com.moseoh.programmers_helper.solution.model.Solution
import com.moseoh.programmers_helper.solution.model.TestCase
import org.jsoup.nodes.Document


@Service
class ParseService(
    private val settings: ProgrammersHelperSettings = ProgrammersHelperSettings.instance
) {

    fun getUrl(urlInput: String): String {
        val sb = StringBuilder()
        sb.append(removeLanguageParameter(urlInput))
        sb.append("?language=")
        sb.append(settings.language.name.lowercase())
        return sb.toString()
    }

    private fun removeLanguageParameter(url: String): String {
        return url.replace(Regex("\\?language=[a-zA-Z+]+"), "")
    }

    fun parseHtml(document: Document): Solution {
        return Solution(
            parseTitle(document),
            parseContent(document),
            parseTestCase(document)
        )
    }

    private fun parseTitle(document: Document): String {
        return document.select("li.algorithm-title").text()
    }

    private fun parseContent(document: Document): String {
        return document.select("textarea#code").text()
    }

    private fun parseTestCase(document: Document): Array<TestCase> {
        val h5Element =
            document.select("h5:containsOwn(입출력 예), h5:has(strong:containsOwn(입출력 예))").first()
                ?: document.select("h3:containsOwn(입출력 예제), h3:has(strong:containsOwn(입출력 예제))").first()
                ?: document.select("h3:containsOwn(예제 입출력), h3:has(strong:containsOwn(예제 입출력))").first()
        val tableElement = h5Element!!.nextElementSibling()

        val tableHeader = tableElement!!.select("thead > tr > th")
        val tableRows = tableElement.select("tbody > tr")

        val content = parseContent(document)
        val paramTypeMap = parseParamType(content)

        return tableRows.map { row ->
            val values = mutableMapOf<String, Any>()
            val columnValues = row.select("td")

            for (i in 0 until tableHeader.size - 1) {
                val key = tableHeader[i].text()
                val value = getValue(key, columnValues[i].text(), paramTypeMap)
                values[key] = value
            }

            val result = parseValue(columnValues.last()!!.text())
            TestCase(values, result)
        }.toTypedArray()
    }

    private fun getValue(key: String, value: String, paramTypeMap: Map<String, String>): Any {
        if (paramTypeMap.contains(key)) {
            when (settings.language) {
                Language.Java -> {
                    when (paramTypeMap[key]) {
                        "int" -> return value.toInt()
                        "long" -> return value.toLong()
                    }
                }

                Language.Kotlin -> {
                    when (paramTypeMap[key]) {
                        "Int" -> return value.toInt()
                        "Long" -> return value.toLong()
                    }
                }
            }
        }

        return parseValue(value)
    }

    private fun parseValue(value: String): Any {
        return when {
            value.startsWith("[[") -> {
                val list = mutableListOf<Array<*>>()
                val pattern = Regex("\\[(.*?)\\]")
                val matchResult = pattern.findAll(value.removeSurrounding("[", "]"))
                matchResult.forEach { result ->
                    list.add(parseValue(result.value) as Array<*>)
                }
                return list.toTypedArray()
            }

            value.startsWith("[") -> {
                val temp = value.removeSurrounding("[", "]")
                val values = if (temp.startsWith("\"")) {
                    val list = mutableListOf<String>()
                    val pattern = Regex("(\".*?\")")
                    val matchRegex = pattern.findAll(temp)
                    matchRegex.forEach { result ->
                        list.add(result.value)
                    }
                    list
                } else {
                    temp.split(",\\s*".toRegex())
                }

                values.map { parseValue(it) }.toTypedArray()
            }

            value.startsWith("\"") -> value.removeSurrounding("\"", "\"")
            value.length == 1 && value[0].isLetter() -> value[0]
            value.toIntOrNull() != null -> value.toInt()
            value.toLongOrNull() != null -> value.toLong()
            value.toFloatOrNull() != null -> value.toFloat()
            value.toDoubleOrNull() != null -> value.toDouble()
            value.toBooleanStrictOrNull() != null -> value.toBoolean()
            else -> value
        }
    }

    fun parseParamType(content: String): Map<String, String> {
        return when (settings.language) {
            Language.Java -> {
                val valueTypeMap = mutableMapOf<String, String>()
                val pattern = Regex("public .* solution\\(.*\\)")
                val funcSignature = pattern.find(content)?.value
                val params = funcSignature?.substringAfter("(")?.substringBeforeLast(")")?.split(", ")
                params?.forEach { param ->
                    val (type, name) = param.trim().split(" ")
                    valueTypeMap[name.trim()] = type.trim()
                }
                valueTypeMap
            }

            Language.Kotlin -> {
                val valueTypeMap = mutableMapOf<String, String>()
                val pattern = Regex("fun solution\\(.*\\): .*")
                val funcSignature = pattern.find(content)?.value
                val params = funcSignature?.substringAfter("(")?.substringBeforeLast(")")?.split(", ")
                params?.forEach { param ->
                    val (name, type) = param.trim().split(":")
                    valueTypeMap[name.trim()] = type.trim()
                }
                valueTypeMap
            }
        }
    }
}
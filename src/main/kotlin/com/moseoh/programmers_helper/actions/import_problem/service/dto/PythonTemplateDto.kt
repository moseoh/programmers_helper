package com.moseoh.programmers_helper.actions.import_problem.service.dto

data class PythonTemplateDto(
    val useMain: Boolean,
    val helpComment: String?,
    val functionName: String,
    val functionContent: String,
    val testCaseDtos: List<TestCaseDto>,
) : ITemplateDto {
    data class TestCaseDto(
        val values: List<Value>,
        val answer: Value,
    )

    data class Value(
        val type: String,
        val name: String,
        val value: String,
    )
}
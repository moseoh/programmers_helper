package com.moseoh.programmers_helper.settings.model

enum class Language(
    val extension: String,
    val template: String,
) {
    Java(".java", "java_template.ftl"),
    Kotlin(".kt", "kotlin_template.ftl"),
    Python3(".py", "python_template.ftl"),
}



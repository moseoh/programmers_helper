package com.moseoh.programmers_helper.actions.import_problem.service.dto

import com.moseoh.programmers_helper.TestData
import com.moseoh.programmers_helper.settings.model.Language
import com.moseoh.programmers_helper.settings.model.ProgrammersHelperSettings
import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test


class PythonTemplateMapperTest {

    private lateinit var pythonTemplateMapper: PythonTemplateMapper

    private fun mocking(
        useFolder: Boolean = false,
        useNameSpacing: Boolean = false,
        useMainFunction: Boolean = false,
        useHelpComment: Boolean = true
    ) {
        mockkObject(ProgrammersHelperSettings)
        every { ProgrammersHelperSettings.state } returns ProgrammersHelperSettings.State()
        every { ProgrammersHelperSettings.state.language } returns Language.Python3
        every { ProgrammersHelperSettings.state.useFolder } returns useFolder
        every { ProgrammersHelperSettings.state.useNameSpacing } returns useNameSpacing
        every { ProgrammersHelperSettings.state.useMainFunction } returns useMainFunction
        every { ProgrammersHelperSettings.state.useHelpComment } returns useHelpComment

        pythonTemplateMapper = PythonTemplateMapper(ProgrammersHelperSettings.state)
    }

    @Test
    fun `toDto basic functionality`() {
        // given
        mocking()
        val problemDto = TestData.problemDto_python_returnSingle()

        // when
        val result = pythonTemplateMapper.toDto("/base/path", "/directory/path", problemDto)

        // then
        assertEquals("solution", result.functionName)
        assertEquals(2, result.testCaseDtos.size)
        assertFalse(result.useMain)  // useMainFunction = false by default
    }

    @Test
    fun `toDto with main function enabled`() {
        // given
        mocking(useMainFunction = true)
        val problemDto = TestData.problemDto_python_returnSingle()

        // when
        val result = pythonTemplateMapper.toDto("/base/path", "/directory/path", problemDto)

        // then
        assertEquals("solution", result.functionName)
        assertEquals(2, result.testCaseDtos.size)
        assertTrue(result.useMain)
    }

    @Test
    fun `toDto with help comment disabled`() {
        // given
        mocking(useHelpComment = false)
        val problemDto = TestData.problemDto_python_returnSingle()

        // when
        val result = pythonTemplateMapper.toDto("/base/path", "/directory/path", problemDto)

        // then
        assertEquals("solution", result.functionName)
        assertEquals(null, result.helpComment) // should be null when disabled
        assertFalse(result.useMain)
    }

    @Test
    fun `toDto string return type`() {
        // given
        mocking()
        val problemDto = TestData.problemDto_python_returnString()

        // when
        val result = pythonTemplateMapper.toDto("/base/path", "/directory/path", problemDto)

        // then
        assertEquals("solution", result.functionName)
        assertEquals(2, result.testCaseDtos.size)
        assertFalse(result.useMain)
    }

    @Test
    fun `toDto list return type`() {
        // given
        mocking()
        val problemDto = TestData.problemDto_python_returnList()

        // when
        val result = pythonTemplateMapper.toDto("/base/path", "/directory/path", problemDto)

        // then
        assertEquals("solution", result.functionName)
        assertEquals(2, result.testCaseDtos.size)
        assertFalse(result.useMain)
    }

    @Test
    fun `toDto nested list return type`() {
        // given
        mocking()
        val problemDto = TestData.problemDto_python_returnListList()

        // when
        val result = pythonTemplateMapper.toDto("/base/path", "/directory/path", problemDto)

        // then
        assertEquals("solution", result.functionName)
        assertEquals(2, result.testCaseDtos.size)
        assertFalse(result.useMain)
    }
}
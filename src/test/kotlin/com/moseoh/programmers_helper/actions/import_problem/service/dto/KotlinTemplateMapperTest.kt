package com.moseoh.programmers_helper.actions.import_problem.service.dto

import com.moseoh.programmers_helper.settings.model.ProgrammersHelperSettings
import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class KotlinTemplateMapperTest {
    private lateinit var kotlinTemplateMapper: KotlinTemplateMapper

    @BeforeEach
    fun setUp() {
        mockkObject(ProgrammersHelperSettings)
        every { ProgrammersHelperSettings.state } returns ProgrammersHelperSettings.State()
        kotlinTemplateMapper = KotlinTemplateMapper(ProgrammersHelperSettings.state)
    }

    @Test
    fun `getValue Int`() {
        // when
        val result = kotlinTemplateMapper.getValue("Int", "1")

        // then
        assertEquals("1", result)
    }

    @Test
    fun `getValue Double`() {
        // when
        val result = kotlinTemplateMapper.getValue("Double", "12.4")

        // then
        assertEquals("12.4", result)
    }

    @Test
    fun `getValue Long`() {
        // when
        val result = kotlinTemplateMapper.getValue("Long", "1")

        // then
        assertEquals("1L", result)
    }

    @Test
    fun `getValue Boolean`() {
        // when
        val result = kotlinTemplateMapper.getValue("Boolean", "false")

        // then
        assertEquals("false", result)
    }

    @Test
    fun `getValue Char`() {
        // when
        val result = kotlinTemplateMapper.getValue("Char", "c")

        // then
        assertEquals("\'c\'", result)
    }

    @Test
    fun `getValue String`() {
        // when
        val result = kotlinTemplateMapper.getValue("String", "str")

        // then
        assertEquals("\"str\"", result)
    }

    @Test
    fun `getValue IntArray`() {
        // when
        val result = kotlinTemplateMapper.getValue("IntArray", "[1, 3]")

        // then
        assertEquals("intArrayOf(1, 3)", result)
    }

    @Test
    fun `getValue DoubleArray`() {
        // when
        val result = kotlinTemplateMapper.getValue("DoubleArray", "[12.4,35.3]")

        // then
        assertEquals("doubleArrayOf(12.4, 35.3)", result)
    }

    @Test
    fun `getValue LongArray`() {
        // when
        val result = kotlinTemplateMapper.getValue("LongArray", "[1,3]")

        // then
        assertEquals("longArrayOf(1L, 3L)", result)
    }

    @Test
    fun `getValue BooleanArray`() {
        // when
        val result = kotlinTemplateMapper.getValue("BooleanArray", "[false, true]")

        // then
        assertEquals("booleanArrayOf(false, true)", result)
    }

    @Test
    fun `getValue CharArray`() {
        // when
        val result = kotlinTemplateMapper.getValue("CharArray", "[c,a]")

        // then
        assertEquals("charArrayOf(\'c\', \'a\')", result)
    }

    @Test
    fun `getValue StringArray`() {
        // when
        val result = kotlinTemplateMapper.getValue("Array<String>", "[\"str\", \"str2\"]")

        // then
        assertEquals("arrayOf(\"str\", \"str2\")", result)
    }

    @Test
    fun `getValue ArrayIntArray`() {
        // when
        val result = kotlinTemplateMapper.getValue("Array<IntArray>", "[[1,3],[4, 4]]")

        // then
        assertEquals("arrayOf(intArrayOf(1, 3), intArrayOf(4, 4))", result)
    }

    @Test
    fun `getValue ArrayDoubleArray`() {
        // when
        val result = kotlinTemplateMapper.getValue("Array<DoubleArray>", "[[1.0,3.1],[4.1, 4.2]]")

        // then
        assertEquals("arrayOf(doubleArrayOf(1.0, 3.1), doubleArrayOf(4.1, 4.2))", result)
    }

    @Test
    fun `getValue ArrayLongArray`() {
        // when
        val result = kotlinTemplateMapper.getValue("Array<LongArray>", "[[1,3],[4, 4]]")

        // then
        assertEquals("arrayOf(longArrayOf(1L, 3L), longArrayOf(4L, 4L))", result)
    }

    @Test
    fun `getValue ArrayBooleanArray`() {
        // when
        val result = kotlinTemplateMapper.getValue("Array<BooleanArray>", "[[false,true],[true, true]]")

        // then
        assertEquals("arrayOf(booleanArrayOf(false, true), booleanArrayOf(true, true))", result)
    }

    @Test
    fun `getValue ArrayCharArray`() {
        // when
        val result = kotlinTemplateMapper.getValue("Array<CharArray>", "[[c, 2],['a', 'b']]")

        // then
        assertEquals("arrayOf(charArrayOf('c', '2'), charArrayOf('a', 'b'))", result)
    }

    @Test
    fun `getValue ArrayStringArray`() {
        // when
        val result = kotlinTemplateMapper.getValue("Array<Array<String>>", "[[1,3],[4, \"str\"]]")

        // then
        assertEquals("arrayOf(arrayOf(\"1\", \"3\"), arrayOf(\"4\", \"str\"))", result)
    }

}
package com.moseoh.programmers_helper.actions.import_problem.service.dto

import com.moseoh.programmers_helper.settings.model.ProgrammersHelperSettings
import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class JavaTemplateMapperTest {
    private lateinit var javaTemplateMapper: JavaTemplateMapper

    @BeforeEach
    fun setUp() {
        mockkObject(ProgrammersHelperSettings)
        every { ProgrammersHelperSettings.state } returns ProgrammersHelperSettings.State()
        javaTemplateMapper = JavaTemplateMapper(ProgrammersHelperSettings.state)
    }

    @Test
    fun `getValue int`() {
        // when
        val result = javaTemplateMapper.getValue("int", "1")

        // then
        assertEquals("1", result)
    }

    @Test
    fun `getValue double`() {
        // when
        val result = javaTemplateMapper.getValue("double", "12.4")

        // then
        assertEquals("12.4", result)
    }

    @Test
    fun `getValue long`() {
        // when
        val result = javaTemplateMapper.getValue("long", "1")

        // then
        assertEquals("1L", result)
    }

    @Test
    fun `getValue boolean`() {
        // when
        val result = javaTemplateMapper.getValue("boolean", "false")

        // then
        assertEquals("false", result)
    }

    @Test
    fun `getValue char`() {
        // when
        val result = javaTemplateMapper.getValue("char", "c")

        // then
        assertEquals("\'c\'", result)
    }

    @Test
    fun `getValue String`() {
        // when
        val result = javaTemplateMapper.getValue("String", "str")

        // then
        assertEquals("\"str\"", result)
    }

    @Test
    fun `getValue intArray`() {
        // when
        val result = javaTemplateMapper.getValue("int[]", "[1, 3]")

        // then
        assertEquals("new int[]{1, 3}", result)
    }

    @Test
    fun `getValue doubleArray`() {
        // when
        val result = javaTemplateMapper.getValue("double[]", "[12.4,35.3]")

        // then
        assertEquals("new double[]{12.4, 35.3}", result)
    }

    @Test
    fun `getValue longArray`() {
        // when
        val result = javaTemplateMapper.getValue("long[]", "[1,3]")

        // then
        assertEquals("new long[]{1L, 3L}", result)
    }

    @Test
    fun `getValue booleanArray`() {
        // when
        val result = javaTemplateMapper.getValue("boolean[]", "[false, true]")

        // then
        assertEquals("new boolean[]{false, true}", result)
    }

    @Test
    fun `getValue charArray`() {
        // when
        val result = javaTemplateMapper.getValue("char[]", "[c,a]")

        // then
        assertEquals("new char[]{\'c\', \'a\'}", result)
    }

    @Test
    fun `getValue StringArray`() {
        // when
        val result = javaTemplateMapper.getValue("String[]", "[\"str\", \"str2\"]")

        // then
        assertEquals("new String[]{\"str\", \"str2\"}", result)
    }

    @Test
    fun `getValue intArrayArray`() {
        // when
        val result = javaTemplateMapper.getValue("int[][]", "[[1,3],[4, 4]]")

        // then
        assertEquals("new int[][]{{1, 3}, {4, 4}}", result)
    }

    @Test
    fun `getValue doubleArrayArray`() {
        // when
        val result = javaTemplateMapper.getValue("double[][]", "[[1.0,3.1],[4.1, 4.2]]")

        // then
        assertEquals("new double[][]{{1.0, 3.1}, {4.1, 4.2}}", result)
    }

    @Test
    fun `getValue longArrayArray`() {
        // when
        val result = javaTemplateMapper.getValue("long[][]", "[[1,3],[4, 4]]")

        // then
        assertEquals("new long[][]{{1L, 3L}, {4L, 4L}}", result)
    }

    @Test
    fun `getValue booleanArrayArray`() {
        // when
        val result = javaTemplateMapper.getValue("boolean[][]", "[[false,true],[true, true]]")

        // then
        assertEquals("new boolean[][]{{false, true}, {true, true}}", result)
    }

    @Test
    fun `getValue charArrayArray`() {
        // when
        val result = javaTemplateMapper.getValue("char[][]", "[[c, 2],['a', 'b']]")

        // then
        assertEquals("new char[][]{{'c', '2'}, {'a', 'b'}}", result)
    }

    @Test
    fun `getValue StringArrayArray`() {
        // when
        val result = javaTemplateMapper.getValue("String[][]", "[[1,3],[4, \"str\"]]")

        // then
        assertEquals("new String[][]{{\"1\", \"3\"}, {\"4\", \"str\"}}", result)
    }
}
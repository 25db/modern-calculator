package com.example.moderncalculator

import com.example.moderncalculator.ui.CalcState
import org.junit.Assert.*
import org.junit.Test

// 纯逻辑单测，不需要 Android 设备
class CalcLogicTest {

    @Test
    fun addition() {
        val c = CalcState()
        c.inputDigit('1'); c.inputDigit('2')
        c.applyOp('+'); c.inputDigit('3')
        c.equals()
        assertEquals("15", c.display)
        assertEquals("12 + 3 =", c.history)
    }

    @Test
    fun subtraction() {
        val c = CalcState()
        c.inputDigit('9'); c.applyOp('−'); c.inputDigit('4')
        c.equals()
        assertEquals("5", c.display)
    }

    @Test
    fun divisionByZero() {
        val c = CalcState()
        c.inputDigit('5'); c.applyOp('÷'); c.inputDigit('0')
        c.equals()
        assertEquals("Error", c.display)
    }

    @Test
    fun clearResets() {
        val c = CalcState()
        c.inputDigit('9'); c.applyOp('+'); c.inputDigit('9')
        c.clear()
        assertEquals("0", c.display)
        assertTrue(c.history.isBlank())
    }

    @Test
    fun toggleSign() {
        val c = CalcState()
        c.inputDigit('5'); c.toggleSign()
        assertEquals("-5", c.display)
        c.toggleSign()
        assertEquals("5", c.display)
    }

    @Test
    fun backspaceNegative() {
        val c = CalcState()
        c.inputDigit('5'); c.toggleSign()
        c.backspace()
        assertEquals("0", c.display)
    }

    @Test
    fun equalsWithoutPendingOpKeepsDisplay() {
        val c = CalcState()
        c.inputDigit('2'); c.applyOp('×'); c.inputDigit('3')
        c.equals()
        assertEquals("6", c.display)
        c.inputDigit('4')          // 新一轮开始
        c.equals()                 // 无 pendingOp，保持 4
        assertEquals("4", c.display)
    }

    @Test
    fun operatorChain() {
        val c = CalcState()
        c.inputDigit('2'); c.applyOp('×'); c.inputDigit('3')
        c.applyOp('+')            // 2×3=6，历史 "6 +"
        c.inputDigit('1')
        c.equals()
        assertEquals("7", c.display)
        assertEquals("6 + 1 =", c.history)
    }

    @Test
    fun pointOnlyOnce() {
        val c = CalcState()
        c.inputDigit('1'); c.inputPoint()
        assertEquals("1.", c.display)
        c.inputPoint()
        assertEquals("1.", c.display)
    }

    @Test
    fun errorThenDigitStartsFresh() {
        val c = CalcState()
        c.inputDigit('1'); c.applyOp('÷'); c.inputDigit('0')
        c.equals()
        assertEquals("Error", c.display)
        c.inputDigit('3')
        assertEquals("3", c.display)
    }
}

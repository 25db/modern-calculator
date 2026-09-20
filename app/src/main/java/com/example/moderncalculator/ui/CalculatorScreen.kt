package com.example.moderncalculator.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moderncalculator.ui.theme.*

// ---------- 计算器状态机（display / history 用 mutableStateOf 驱动重组） ----------
class CalcState {
    var display by mutableStateOf("0")
    var history by mutableStateOf("")
    var acc: Double? = null
    var pendingOp: Char? = null
    var waitingForOperand = false

    fun inputDigit(d: Char) {
        display = when {
            waitingForOperand -> { waitingForOperand = false; d.toString() }
            display == "Error" -> d.toString()
            display == "0" -> d.toString()
            display.length < 15 -> display + d
            else -> display
        }
    }

    fun inputPoint() {
        when {
            waitingForOperand -> { display = "0."; waitingForOperand = false }
            display == "Error" -> { display = "0."; waitingForOperand = false }
            !display.contains(".") -> display += "."
        }
    }

    fun clear() { display = "0"; acc = null; pendingOp = null; waitingForOperand = false; history = "" }

    fun toggleSign() {
        if (display != "0" && display != "Error")
            display = if (display.startsWith("-")) display.substring(1) else "-" + display
    }

    fun backspace() {
        if (waitingForOperand || display == "Error") { display = "0"; waitingForOperand = false; return }
        display = when {
            display.length > 2 && display[0] == '-' -> display.dropLast(1)
            display.length == 2 && display[0] == '-' -> "0"
            display.length > 1 -> display.dropLast(1)
            else -> "0"
        }
    }

    fun applyOp(op: Char) {
        val current = display.toDoubleOrNull() ?: return
        when {
            acc != null && pendingOp != null && !waitingForOperand -> {
                val r = compute(acc!!, current, pendingOp!!)
                display = fmt(r); acc = r; history = "${fmt(r)} $op"
            }
            waitingForOperand -> pendingOp = op
            else -> { acc = current; history = fmt(current) }
        }
        pendingOp = op
        waitingForOperand = true
    }

    fun equals() {
        val a = acc; val o = pendingOp
        if (a == null || o == null) return
        val b = display.toDoubleOrNull() ?: return
        val r = compute(a, b, o)
        history = "${fmt(a)} $o ${fmt(b)} ="
        display = fmt(r); acc = r; pendingOp = null; waitingForOperand = true
    }

    private fun compute(a: Double, b: Double, op: Char) = when (op) {
        '+' -> a + b
        '−', '-' -> a - b
        '×' -> a * b
        '÷' -> if (b == 0.0) Double.NaN else a / b
        else -> a
    }

    private fun fmt(v: Double): String {
        if (v.isNaN() || v.isInfinite()) return "Error"
        return if (v % 1 == 0.0 && Math.abs(v) < 1e15) v.toLong().toString() else v.toString()
    }
}

// ---------- 屏幕 ----------
@Composable
fun CalculatorScreen() {
    val state = remember { CalcState() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CalcBackground)
            .padding(horizontal = 16.dp)
    ) {
        // 标题
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("C A L C U L A T O R", color = CalcSubText,
                fontSize = 10.sp, letterSpacing = 4.sp, fontWeight = FontWeight.Medium)
            Text(
                "⟲", color = CalcSubText, fontSize = 14.sp,
                modifier = Modifier
                    .clickable { state.clear() }
                    .padding(6.dp)
            )
        }

        // 显示区
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(CalcDisplay)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (state.history.isNotBlank()) state.history else " ",
                color = CalcSubText, fontSize = 13.sp,
                modifier = Modifier.padding(top = 10.dp)
            )
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                val d = state.display
                val fs = when {
                    d.length > 12 -> 30.sp
                    d.length > 9  -> 38.sp
                    d.length > 6  -> 46.sp
                    else -> 58.sp
                }
                Text(
                    text = d,
                    color = CalcText,
                    fontSize = fs,
                    fontWeight = FontWeight.W300,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }

        Spacer(Modifier.height(18.dp))

        // 按钮网格
        val gap = 12.dp
        Column(verticalArrangement = Arrangement.spacedBy(gap), modifier = Modifier.weight(1f)) {
            row4 {
                CalcButton("C", CalcFn) { state.clear() }
                CalcButton("±", CalcFn) { state.toggleSign() }
                CalcButton("⌫", CalcFn) { state.backspace() }
                CalcButton("÷", CalcOp) { state.applyOp('÷') }
            }
            row4 {
                CalcButton("7", CalcIdle) { state.inputDigit('7') }
                CalcButton("8", CalcIdle) { state.inputDigit('8') }
                CalcButton("9", CalcIdle) { state.inputDigit('9') }
                CalcButton("×", CalcOp) { state.applyOp('×') }
            }
            row4 {
                CalcButton("4", CalcIdle) { state.inputDigit('4') }
                CalcButton("5", CalcIdle) { state.inputDigit('5') }
                CalcButton("6", CalcIdle) { state.inputDigit('6') }
                CalcButton("−", CalcOp) { state.applyOp('−') }
            }
            row4 {
                CalcButton("1", CalcIdle) { state.inputDigit('1') }
                CalcButton("2", CalcIdle) { state.inputDigit('2') }
                CalcButton("3", CalcIdle) { state.inputDigit('3') }
                CalcButton("+", CalcOp) { state.applyOp('+') }
            }
            Row(modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalArrangement = Arrangement.spacedBy(gap)) {
                CalcButton("0", CalcIdle, isWide = true) { state.inputDigit('0') }
                CalcButton(".", CalcIdle) { state.inputPoint() }
                CalcButton("=", CalcEquals) { state.equals() }
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}

// 4列行（占满整列高度）
@Composable
fun RowScope.row4(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .padding(bottom = 0.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) { content() }
}

// 单个按钮
@Composable
fun CalcButton(label: String, bg: Color, isWide: Boolean = false, onClick: () -> Unit) {
    val textFg = when {
        bg == CalcOp -> Color(0xFF1A1D24)
        bg == CalcEquals -> Color(0xFF0F1115)
        else -> CalcText
    }
    Row(
        modifier = Modifier
            .weight(if (isWide) 2f else 1f)
            .fillMaxHeight()
            .clip(RoundedCornerShape(16.dp))
            .background(bg)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
        contentPadding = PaddingValues(end = 14.dp)
    ) {
        Text(label, color = textFg, fontSize = 20.sp, fontWeight = FontWeight.Medium)
    }
}

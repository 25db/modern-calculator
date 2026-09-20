package com.example.moderncalculator.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// 现代深色计算器配色
val CalcBackground = Color(0xFF0F1115)      // 深底
val CalcCard = Color(0xFF1A1D24)             // 卡片背景
val CalcDisplay = Color(0xFF12141A)          // 显示区
val CalcIdle = Color(0xFF2A2E39)             // 普通按钮（数字）
val CalcOp = Color(0xFFFFB545)               // 运算符
val CalcFn = Color(0xFF4C566A)               // 功能键（C, ±）
val CalcEquals = Color(0xFF40C4FF)          // 等号
val CalcText = Color(0xFFF2F5FA)             // 主文字
val CalcSubText = Color(0xFF8B93A7)         // 次级文字

private val LightColorScheme = lightColorScheme()
private val DarkColorScheme = darkColorScheme()

@Composable
fun ModernCalculatorTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        content = content
    )
}

package com.example.moderncalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.moderncalculator.ui.CalculatorScreen
import com.example.moderncalculator.ui.theme.ModernCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ModernCalculatorTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    CalculatorScreen()
                }
            }
        }
    }
}

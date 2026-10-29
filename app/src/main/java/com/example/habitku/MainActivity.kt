package com.example.habitku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.habitku.ui.theme.HabitKuTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge() // Mengaktifkan mode Edge-to-Edge agar UI dapat dirender di balik system bar
        setContent { // Menentukan antarmuka Compose yang akan ditampilkan
            HabitKuTheme {  // Menerapkan tema aplikasi HabitKuTheme
                HabitRoute()  // Menampilkan layar utama aplikasi yang dikelola oleh HabitRoute
            }
        }
    }
}
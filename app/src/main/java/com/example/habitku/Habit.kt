package com.example.habitku

/** Data class yang merepresentasikan sebuah entitas kebiasaan (habit).*/
data class Habit(
    val id: String,                  // ID unik untuk setiap habit
    val name: String,                // Nama atau deskripsi kebiasaan
    val isCompleted: Boolean = false // Status apakah habit sudah selesai dikerjakan atau belum
)

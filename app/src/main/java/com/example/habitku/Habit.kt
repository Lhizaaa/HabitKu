package com.example.habitku

data class Habit(
    val id: String,
    val name: String,
    val isCompleted: Boolean = false
)

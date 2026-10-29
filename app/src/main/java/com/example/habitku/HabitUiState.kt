package com.example.habitku

data class HabitUiState(
    val habits: List<Habit> = emptyList(),
    val draftName: String = "",
    val inputError: String? = null
)

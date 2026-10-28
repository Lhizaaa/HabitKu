package com.example.habitku

/**
 * Data class untuk merepresentasikan state UI pada layar Habit.
 * Menyimpan seluruh data yang dibutuhkan oleh UI untuk dirender.
 */
data class HabitUiState(
    val habits: List<Habit> = emptyList(), // Daftar seluruh kebiasaan yang tersimpan
    val draftName: String = "",            // Teks sementara yang sedang diketik di input field
    val inputError: String? = null         // Pesan error validasi input (jika ada)
)

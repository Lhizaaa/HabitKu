package com.example.habitku

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

/**ViewModel untuk mengelola state dan business logic dari aplikasi HabitKu.
 Mewarisi [ViewModel] agar state tetap terjaga saat terjadi perubahan konfigurasi (seperti rotasi layar).
 */
class HabitViewModel : ViewModel() {

    // MutableStateFlow privat untuk menyimpan dan mengubah UI state secara internal
    private val _uiState = MutableStateFlow(HabitUiState())

    // StateFlow publik (read-only) yang diobservasi oleh UI (Composable)
    val uiState: StateFlow<HabitUiState> = _uiState.asStateFlow()

    /** Dipanggil ketika teks nama kebiasaan pada input berubah.
    Memperbarui nilai draftName dan menghapus pesan error jika ada.*/
    fun onDraftNameChanged(newName: String) {
        _uiState.update { current ->
            current.copy(draftName = newName, inputError = null)
        }
    }

    /**Dipanggil ketika pengguna ingin menambahkan kebiasaan baru.
      Melakukan validasi input (tidak boleh kosong) dan membuat objek [Habit] baru.*/
    fun onAddHabit() {
        _uiState.update { current ->
            val name = current.draftName.trim()

            if (name.isBlank()) {
                // Jika kosong, tampilkan pesan error
                current.copy(inputError = "Nama kebiasaan tidak boleh kosong")
            } else {
                // Jika valid, buat habit baru dengan UUID unik dan tambahkan ke daftar
                val newHabit = Habit(id = UUID.randomUUID().toString(), name = name)
                current.copy(
                    habits = current.habits + newHabit,
                    draftName = "", // Reset input text
                    inputError = null
                )
            }
        }
    }

    /**
    Dipanggil ketika status centang (selesai/belum) pada suatu kebiasaan diubah */
    fun onHabitCompletionChanged(id: String, isCompleted: Boolean) {
        _uiState.update { current ->
            current.copy(
                habits = current.habits.map { habit ->
                    if (habit.id == id) habit.copy(isCompleted = isCompleted) else habit
                }
            )
        }
    }

    /**
    Dipanggil untuk menghapus kebiasaan berdasarkan ID-nya.*/
    fun onDeleteHabit(id: String) {
        _uiState.update { current ->
            current.copy(
                habits = current.habits.filter { it.id != id }
            )
        }
    }
}

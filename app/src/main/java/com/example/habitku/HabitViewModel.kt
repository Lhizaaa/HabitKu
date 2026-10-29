package com.example.habitku

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class HabitViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HabitUiState())
    val uiState: StateFlow<HabitUiState> = _uiState.asStateFlow()

    fun onDraftNameChanged(newName: String) {
        _uiState.update { current ->
            current.copy(draftName = newName, inputError = null)
        }
    }

    fun onAddHabit() {
        _uiState.update { current ->
            val name = current.draftName.trim()

            if (name.isBlank()) {
                current.copy(inputError = "Nama kebiasaan tidak boleh kosong")
            } else {
                val newHabit = Habit(id = UUID.randomUUID().toString(), name = name)
                current.copy(
                    habits = current.habits + newHabit,
                    draftName = "",
                    inputError = null
                )
            }
        }
    }

    fun onHabitCompletionChanged(id: String, isCompleted: Boolean) {
        _uiState.update { current ->
            current.copy(
                habits = current.habits.map { habit ->
                    if (habit.id == id) habit.copy(isCompleted = isCompleted) else habit
                }
            )
        }
    }

    fun onDeleteHabit(id: String) {
        _uiState.update { current ->
            current.copy(
                habits = current.habits.filter { it.id != id }
            )
        }
    }
}

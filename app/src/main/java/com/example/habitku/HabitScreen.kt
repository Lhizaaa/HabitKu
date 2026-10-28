package com.example.habitku

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HabitRoute(viewModel: HabitViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HabitScreen(
        uiState = uiState,
        onDraftNameChanged = viewModel::onDraftNameChanged,
        onAddHabit = viewModel::onAddHabit,
        onHabitCompletionChanged = viewModel::onHabitCompletionChanged,
        onDeleteHabit = viewModel::onDeleteHabit
    )
}

@Composable
fun HabitScreen(
    uiState: HabitUiState,
    onDraftNameChanged: (String) -> Unit,
    onAddHabit: () -> Unit,
    onHabitCompletionChanged: (String, Boolean) -> Unit,
    onDeleteHabit: (String) -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            val isWideScreen = maxWidth >= 600.dp

            if (isWideScreen) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text("Daftar Kebiasaan", style = MaterialTheme.typography.headlineMedium)
                        InputForm(
                            draftName = uiState.draftName,
                            inputError = uiState.inputError,
                            onDraftNameChanged = onDraftNameChanged,
                            onAddHabit = onAddHabit
                        )
                    }

                    Column(modifier = Modifier.weight(1.5f).fillMaxHeight()) {
                        HabitListContent(
                            habits = uiState.habits,
                            onHabitCompletionChanged = onHabitCompletionChanged,
                            onDeleteHabit = onDeleteHabit
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Daftar Kebiasaan", style = MaterialTheme.typography.headlineMedium)
                    InputForm(
                        draftName = uiState.draftName,
                        inputError = uiState.inputError,
                        onDraftNameChanged = onDraftNameChanged,
                        onAddHabit = onAddHabit
                    )
                    HabitListContent(
                        habits = uiState.habits,
                        onHabitCompletionChanged = onHabitCompletionChanged,
                        onDeleteHabit = onDeleteHabit,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun InputForm(
    draftName: String,
    inputError: String?,
    onDraftNameChanged: (String) -> Unit,
    onAddHabit: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = draftName,
            onValueChange = onDraftNameChanged,
            label = { Text("Nama kebiasaan baru") },
            isError = inputError != null,
            supportingText = { inputError?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = onAddHabit, modifier = Modifier.fillMaxWidth()) {
            Text("Tambah Kebiasaan")
        }
    }
}

@Composable
private fun HabitListContent(
    habits: List<Habit>,
    onHabitCompletionChanged: (String, Boolean) -> Unit,
    onDeleteHabit: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (habits.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "Belum ada kebiasaan. Tambahkan kebiasaan baik pertama Anda!",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        LazyColumn(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(habits, key = { it.id }) { habit ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = habit.isCompleted,
                            onCheckedChange = { isChecked ->
                                onHabitCompletionChanged(habit.id, isChecked)
                            }
                        )
                        Text(
                            text = habit.name,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { onDeleteHabit(habit.id) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Hapus Kebiasaan",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}
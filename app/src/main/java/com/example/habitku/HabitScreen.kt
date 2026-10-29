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

/** Stateful Composable yang berfungsi sebagai pembungkus (Route).
  Menghubungkan (HabitViewModel) dengan UI (HabitScreen) serta mengobservasi (HabitUiState)
  secara lifecycle-aware menggunakan `collectAsStateWithLifecycle`. */
@Composable
fun HabitRoute(viewModel: HabitViewModel = viewModel()) {
    // Mengobservasi UI state dari ViewModel secara reaktif
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HabitScreen(
        uiState = uiState,
        onDraftNameChanged = viewModel::onDraftNameChanged,
        onAddHabit = viewModel::onAddHabit,
        onHabitCompletionChanged = viewModel::onHabitCompletionChanged,
        onDeleteHabit = viewModel::onDeleteHabit
    )
}

/** Stateless Composable utama untuk menampilkan antarmuka aplikasi.
  Mengimplementasikan Adaptive Layout yang menyesuaikan tata letak berdasarkan lebar layar:
  - Layar Lebar Tablet: Menampilkan 2 kolom berdampingan.
  - Layar Sempit HP Potret: Menampilkan 1 kolom vertikal bertumpuk. */
@Composable
fun HabitScreen(
    uiState: HabitUiState,
    onDraftNameChanged: (String) -> Unit,
    onAddHabit: () -> Unit,
    onHabitCompletionChanged: (String, Boolean) -> Unit,
    onDeleteHabit: (String) -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        // BoxWithConstraints digunakan untuk mengetahui ukuran layar yang tersedia
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Cek apakah lebar layar tergolong layar lebar (>= 600dp)
            val isWideScreen = maxWidth >= 600.dp

            if (isWideScreen) {
                // Layout 2 Kolom Berdampingan untuk Layar Lebar (Tablet / Lanskap)
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Kolom Kiri: Formulir Input
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

                    // Kolom Kanan: Daftar Kebiasaan
                    Column(modifier = Modifier.weight(1.5f).fillMaxHeight()) {
                        HabitListContent(
                            habits = uiState.habits,
                            onHabitCompletionChanged = onHabitCompletionChanged,
                            onDeleteHabit = onDeleteHabit
                        )
                    }
                }
            } else {
                // Layout 1 Kolom Vertikal untuk Layar Sempit (HP Potret)
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

/** Komponen formulir untuk memasukkan nama kebiasaan baru.
  Berisi kolom input teks (OutlinedTextField) dan tombol (Button) untuk menambah kebiasaan */
@Composable
private fun InputForm(
    draftName: String,
    inputError: String?,
    onDraftNameChanged: (String) -> Unit,
    onAddHabit: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Kolom isian teks nama kebiasaan baru
        OutlinedTextField(
            value = draftName,
            onValueChange = onDraftNameChanged,
            label = { Text("Nama kebiasaan baru") },
            isError = inputError != null,
            supportingText = { inputError?.let { Text(it) } }, // Menampilkan pesan error validasi jika ada
            modifier = Modifier.fillMaxWidth()
        )
        // Tombol untuk memicu penambahan kebiasaan baru
        Button(onClick = onAddHabit, modifier = Modifier.fillMaxWidth()) {
            Text("Tambah Kebiasaan")
        }
    }
}

/** Komponen untuk menampilkan daftar kebiasaan yang tersimpan.
 Menampilkan pesan 'Empty State' jika daftar kosong,
 atau daftar [LazyColumn] berisi kartu kebiasaan jika data tersedia. */
@Composable
private fun HabitListContent(
    habits: List<Habit>,
    onHabitCompletionChanged: (String, Boolean) -> Unit,
    onDeleteHabit: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (habits.isEmpty()) {
        // Tampilan Empty State ketika belum ada kebiasaan
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "Belum ada kebiasaan. Tambahkan kebiasaan baik pertama Anda!",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        // Tampilan Daftar menggunakan LazyColumn untuk performa scroll yang efisien
        LazyColumn(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(habits, key = { it.id }) { habit ->
                // Kartu item kebiasaan
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Checkbox status selesai
                        Checkbox(
                            checked = habit.isCompleted,
                            onCheckedChange = { isChecked ->
                                onHabitCompletionChanged(habit.id, isChecked)
                            }
                        )
                        // Teks nama kebiasaan
                        Text(
                            text = habit.name,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f)
                        )
                        // Tombol ikon hapus
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
package com.example.a20_2_fab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.em
import androidx.compose.ui.window.Dialog


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Notes()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Notes() {

    var notes by rememberSaveable { mutableStateOf(listOf<String>()) }
    var noteText by rememberSaveable { mutableStateOf("") }
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }
    var noteToDeleteIndex by rememberSaveable { mutableIntStateOf(-1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Заметки", letterSpacing = 0.5.em, textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (noteText.isNotBlank()) {
                        notes = notes + noteText
                        noteText = ""
                    }
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Добавить заметку")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            val focusManager = LocalFocusManager.current

            OutlinedTextField(
                value = noteText,
                onValueChange = { noteText = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Введите текст заметки") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(notes.size) { index ->
                    NoteItem(
                        note = notes[index],
                        onDelete = {
                            noteToDeleteIndex = index
                            showDeleteDialog = true
                        }
                    )
                }
            }
        }

        if (showDeleteDialog) {
            Dialog(onDismissRequest = { showDeleteDialog = false }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Удаление",
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Удалить заметку?", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Вы уверены, что хотите удалить эту заметку?")
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TextButton(onClick = { showDeleteDialog = false }) {
                                Text("Отмена", style = MaterialTheme.typography.bodyMedium)
                            }
                            TextButton(onClick = {
                                notes = notes.toMutableList().apply { removeAt(noteToDeleteIndex) }
                                showDeleteDialog = false
                            }) {
                                Text("Удалить", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun NoteItem(note: String, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = note,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Удалить заметку")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewNotes() {
    Notes()
}
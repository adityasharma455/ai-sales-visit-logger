package com.example.smartsalesvisit.presentation.screens.utils


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    label: String = "Select Date",
    onDateSelected: (Long, String) -> Unit
) {

    var showDialog by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("") }

    val datePickerState = rememberDatePickerState()

    val formatter = remember {
        SimpleDateFormat("dd/MM/yyyy", Locale("en", "IN"))
    }

    // 🔥 UI FIELD (CLICKABLE)
    OutlinedTextField(
        value = selectedText,
        onValueChange = {},
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDialog = true },
        enabled = false, // ❌ disable typing
        singleLine = true
    )

    // 🔥 DATE PICKER DIALOG
    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val millis = datePickerState.selectedDateMillis
                        if (millis != null) {
                            val formatted = formatter.format(Date(millis))
                            selectedText = formatted
                            onDateSelected(millis, formatted)
                        }
                        showDialog = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
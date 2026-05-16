package com.example.smartsalesvisit.presentation.screens.AddVisitScreen

import android.Manifest
import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartsalesvisit.common.AudioRecorderManager
import com.example.smartsalesvisit.common.playRecording
import com.example.smartsalesvisit.domain.models.Visit
import com.example.smartsalesvisit.presentation.screens.utils.DatePickerField
import org.koin.compose.viewmodel.koinViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterVisitScreen(
    viewModel: RegisterVisitViewModel = koinViewModel(),
    onVisitAdded: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val speechState by viewModel.speechToTextState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var customerName by rememberSaveable { mutableStateOf("") }
    var contactPerson by rememberSaveable { mutableStateOf("") }
    var location by rememberSaveable { mutableStateOf("") }
    var territory by rememberSaveable { mutableStateOf("") }
    var notes by rememberSaveable { mutableStateOf("") }

    var outcomeStatus by rememberSaveable { mutableStateOf("Completed") }
    var followUpDate by rememberSaveable { mutableStateOf("") }

    var customerEmail by rememberSaveable { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }

    val recorder = remember { AudioRecorderManager(context) }
    var isRecording by remember { mutableStateOf(false) }
    var audioPath by remember { mutableStateOf<String?>(null) }

    val outcomeOptions = listOf(
        "Completed",
        "Follow-up needed",
        "No interest"
    )

    val gradient = Brush.verticalGradient(
        listOf(
            Color(0xFF0F2027),
            Color(0xFF203A43),
            Color(0xFF2C5364)
        )
    )

    LaunchedEffect(state.Success) {
        if (state.Success == true) {
            onVisitAdded()
        }
    }

    LaunchedEffect(speechState) {
        speechState.Success?.let {
            notes = it
            Toast.makeText(context, "Transcription Completed", Toast.LENGTH_SHORT).show()
        }

        speechState.Error?.let {
            if (it.isNotBlank()) {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            audioPath = recorder.startRecording()
            isRecording = true
            Toast.makeText(context, "Recording Started", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Create Visit Log",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(padding)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {

                        OutlinedTextField(
                            value = customerName,
                            onValueChange = { customerName = it },
                            label = { Text("Customer Name") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = contactPerson,
                            onValueChange = { contactPerson = it },
                            label = { Text("Contact Person") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = customerEmail,
                            onValueChange = {
                                customerEmail = it
                                emailError = null
                            },
                            label = { Text("Customer Email") },
                            isError = emailError != null,
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (emailError != null) {
                            Text(
                                text = emailError!!,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        OutlinedTextField(
                            value = territory,
                            onValueChange = { territory = it },
                            label = { Text("Enter Your Territory") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = location,
                            onValueChange = { location = it },
                            label = { Text("Location") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = notes,
                            onValueChange = { notes = it },
                            label = { Text("Raw Meeting Notes") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = {
                                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                },
                                enabled = !isRecording
                            ) {
                                Text("🎤 Start Recording")
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = {
                                    audioPath = recorder.stopRecording()
                                    isRecording = false

                                    Toast.makeText(
                                        context,
                                        "Recording saved",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    audioPath?.let { path ->
                                        viewModel.transcribeAudioToNotes(path)
                                    }
                                },
                                enabled = isRecording
                            ) {
                                Text("⏹ Stop")
                            }
                        }

                        Button(
                            onClick = {
                                audioPath?.let {
                                    playRecording(it)
                                } ?: Toast.makeText(
                                    context,
                                    "No recording found",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            enabled = !isRecording && audioPath != null
                        ) {
                            Text("Play Recording")
                        }

                        if (speechState.isLoading) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator()
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Converting speech to text...")
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Outcome Status",
                            style = MaterialTheme.typography.titleMedium
                        )

                        outcomeOptions.forEach { option ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = outcomeStatus == option,
                                    onClick = { outcomeStatus = option }
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = option,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }

                        if (outcomeStatus == "Follow-up needed") {
                            DatePickerField(
                                label = "Follow-up Date"
                            ) { _, formatted ->
                                followUpDate = formatted
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        val isFormValid =
                            customerName.isNotBlank() &&
                                    contactPerson.isNotBlank() &&
                                    location.isNotBlank() &&
                                    customerEmail.isNotBlank() &&
                                    isValidEmail(customerEmail) &&
                                    territory.isNotBlank() &&
                                    (outcomeStatus != "Follow-up needed" || followUpDate.isNotBlank())

                        Button(
                            onClick = {
                                if (!isValidEmail(customerEmail)) {
                                    emailError = "Invalid email format"
                                    return@Button
                                }

                                val visit = Visit(
                                    id = UUID.randomUUID().toString(),
                                    salesPersonId = "localUser",
                                    customerName = customerName,
                                    contactPerson = contactPerson,
                                    customerEmail = customerEmail,
                                    location = location,
                                    visitDate = System.currentTimeMillis(),
                                    territory = territory,
                                    rawNotes = notes,
                                    audioFilePath = audioPath,
                                    meetingSummary = null,
                                    painPoints = null,
                                    actionItems = null,
                                    nextStep = null,
                                    customerEmotion = null,
                                    dealProbability = null,
                                    suggestedStrategy = null,
                                    outcomeStatus = outcomeStatus,
                                    followUpDate = followUpDate,
                                    aiStatus = "None",
                                    syncStatus = "draft"
                                )

                                viewModel.addVisit(visit)
                            },
                            enabled = isFormValid,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                        ) {
                            Text(
                                text = "Save Visit",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
            }

            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}
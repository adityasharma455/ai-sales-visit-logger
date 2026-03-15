package com.example.smartsalesvisit.presentation.screens.AddVisitScreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartsalesvisit.domain.models.Visit
import org.koin.compose.viewmodel.koinViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterVisitScreen(
    viewModel: RegisterVisitViewModel = koinViewModel(),
    onVisitAdded: () -> Unit
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var customerName by rememberSaveable { mutableStateOf("") }
    var contactPerson by rememberSaveable { mutableStateOf("") }
    var location by rememberSaveable { mutableStateOf("") }
    var notes by rememberSaveable { mutableStateOf("") }

    var outcomeStatus by rememberSaveable { mutableStateOf("Completed") }
    var followUpDate by rememberSaveable { mutableStateOf("") }

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
                        modifier = Modifier
                            .padding(20.dp),
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

                            OutlinedTextField(
                                value = followUpDate,
                                onValueChange = { followUpDate = it },
                                label = { Text("Follow-up Date") },
                                modifier = Modifier.fillMaxWidth()
                            )

                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {

                                if (customerName.isBlank()) {
                                    Toast.makeText(context, "Enter Customer Name", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }

                                if (outcomeStatus == "Follow-up needed" && followUpDate.isBlank()) {
                                    Toast.makeText(context, "Follow-up date required", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }

                                val visit = Visit(
                                    id = UUID.randomUUID().toString(),
                                    salesPersonId = "localUser",
                                    customerName = customerName,
                                    contactPerson = contactPerson,
                                    location = location,
                                    visitDate = System.currentTimeMillis(),
                                    rawNotes = notes,
                                    meetingSummary = null,
                                    painPoints = null,
                                    actionItems = null,
                                    nextStep = null,
                                    outcomeStatus = outcomeStatus,
                                    followUpDate = followUpDate,
                                    aiStatus = "None",
                                    syncStatus = "draft"
                                )

                                viewModel.addVisit(visit)

                            },
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
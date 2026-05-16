package com.example.smartsalesvisit.presentation.screens.UpdateVisits

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import java.text.SimpleDateFormat
import java.util.*
import android.util.Patterns

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateVisitScreen(
    visit: Visit,
    onVisitUpdated: () -> Unit,
    viewModel: UpdateVisitsViewModel = koinViewModel()
){

    val state by viewModel.updateVisitsState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val formatter = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())

    var customerName by rememberSaveable { mutableStateOf(visit.customerName) }
    var contactPerson by rememberSaveable { mutableStateOf(visit.contactPerson) }
    var location by rememberSaveable { mutableStateOf(visit.location) }
    var visitDate by rememberSaveable { mutableStateOf(formatter.format(Date(visit.visitDate))) }
    var rawNotes by rememberSaveable { mutableStateOf(visit.rawNotes) }
    var territory by rememberSaveable { mutableStateOf(visit.territory ?: "") }

    var meetingSummary by rememberSaveable { mutableStateOf(visit.meetingSummary ?: "") }
    var painPoints by rememberSaveable { mutableStateOf(visit.painPoints ?: "") }
    var actionItems by rememberSaveable { mutableStateOf(visit.actionItems ?: "") }
    var nextStep by rememberSaveable { mutableStateOf(visit.nextStep ?: "") }

    var customerEmotion by rememberSaveable { mutableStateOf(visit.customerEmotion ?: "") }
    var dealProbability by rememberSaveable { mutableStateOf(visit.dealProbability ?: "") }
    var suggestedStrategy by rememberSaveable { mutableStateOf(visit.suggestedStrategy ?: "") }

    var outcomeStatus by rememberSaveable { mutableStateOf(visit.outcomeStatus) }
    var followUpDate by rememberSaveable { mutableStateOf(visit.followUpDate ?: " ") }

    var customerEmail by rememberSaveable { mutableStateOf(visit.customerEmail) }
    var emailError by remember { mutableStateOf<String?>(null) }

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

    LaunchedEffect(state.Success){

        if(state.Success == true){
            Toast.makeText(context,"Visit Updated Successfully",Toast.LENGTH_LONG).show()
            onVisitUpdated()
        }

        if(state.Error != null){
            Toast.makeText(context,state.Error,Toast.LENGTH_LONG).show()
        }

    }



    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->

        if (result.resultCode == Activity.RESULT_OK && result.data != null) {

            val text = result.data
                ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                ?.firstOrNull()

            if (!text.isNullOrBlank()) {
                rawNotes = rawNotes + " " + text   // append to notes
            } else {
                Toast.makeText(context, "No speech detected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(

        topBar = {

            TopAppBar(
                title = {
                    Text(
                        text = "Update Visit",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )

        }

    ){ padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(padding)
        ){

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ){

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(6.dp)
                ){

                    Column(
                        modifier = Modifier
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ){

                        OutlinedTextField(
                            value = customerName,
                            onValueChange = {customerName = it},
                            label = {Text("Customer Name")},
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = contactPerson,
                            onValueChange = {contactPerson = it},
                            label = {Text("Contact Person")},
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
                            onValueChange = {location = it},
                            label = {Text("Location")},
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = visitDate,
                            onValueChange = {},
                            label = {Text("Visit Date")},
                            enabled = false,
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = rawNotes,
                            onValueChange = {rawNotes = it},
                            label = {Text("Raw Meeting Notes")},
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(onClick = {

                                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                    putExtra(
                                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                                    )
                                    putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak meeting notes")
                                }

                                speechLauncher.launch(intent)

                            }) {
                                Text("🎤 Speak")
                            }

                        }


                        OutlinedTextField(
                            value = meetingSummary,
                            onValueChange = {meetingSummary = it},
                            label = {Text("Meeting Summary")},
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = painPoints,
                            onValueChange = {painPoints = it},
                            label = {Text("Pain Points")},
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = actionItems,
                            onValueChange = {actionItems = it},
                            label = {Text("Action Items")},
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = nextStep,
                            onValueChange = {nextStep = it},
                            label = {Text("Next Step")},
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = customerEmotion,
                            onValueChange = { customerEmotion = it },
                            label = { Text("Customer Emotion") }
                        )

                        OutlinedTextField(
                            value = dealProbability,
                            onValueChange = { dealProbability = it },
                            label = { Text("Deal Probability") }
                        )

                        OutlinedTextField(
                            value = suggestedStrategy,
                            onValueChange = { suggestedStrategy = it },
                            label = { Text("Suggested Strategy") }
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Outcome Status",
                            style = MaterialTheme.typography.titleMedium
                        )

                        outcomeOptions.forEach { option ->

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ){

                                RadioButton(
                                    selected = outcomeStatus == option,
                                    onClick = { outcomeStatus = option }
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(option)

                            }

                        }

                        if(outcomeStatus == "Follow-up needed"){

                            OutlinedTextField(
                                value = followUpDate,
                                onValueChange = {followUpDate = it},
                                label = {Text("Follow Up Date")},
                                modifier = Modifier.fillMaxWidth()
                            )

                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        val isFormValid =
                            customerName.isNotBlank() &&
                                    contactPerson.isNotBlank() &&
                                    customerEmail.isNotBlank() &&
                                    isValidEmail(customerEmail) &&
                                    location.isNotBlank() &&
                                    territory.isNotBlank() &&
                                    rawNotes.isNotBlank() &&
                                    (outcomeStatus != "Follow-up needed" || followUpDate.isNotBlank())

                        Button(
                            onClick = {

                                if (!isValidEmail(customerEmail)) {
                                    emailError = "Invalid email format"
                                    return@Button
                                }

                                val updatedVisit = visit.copy(
                                    customerName = customerName,
                                    contactPerson = contactPerson,
                                    customerEmail = customerEmail,
                                    location = location,
                                    visitDate = System.currentTimeMillis(),
                                    territory = territory,
                                    rawNotes = rawNotes,
                                    meetingSummary = meetingSummary,
                                    painPoints = painPoints,
                                    actionItems = actionItems,
                                    customerEmotion = customerEmotion,
                                    dealProbability = dealProbability,
                                    suggestedStrategy = suggestedStrategy,
                                    outcomeStatus = outcomeStatus,
                                    followUpDate = followUpDate
                                )

                                viewModel.updateVisits(updatedVisit)

                            },
                            enabled = isFormValid,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                        ){
                            Text("Update Visit")
                        }

                    }

                }

                Spacer(modifier = Modifier.height(40.dp))

            }

            if(state.isLoading == true){

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    CircularProgressIndicator()
                }

            }

        }

    }

}

fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}
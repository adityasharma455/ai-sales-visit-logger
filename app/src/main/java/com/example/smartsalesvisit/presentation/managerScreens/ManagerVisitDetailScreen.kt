package com.example.smartsalesvisit.presentation.managerScreens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.smartsalesvisit.domain.models.Visit
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerVisitDetailScreen(
    visit: Visit
) {

    val formatter = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())

    val gradient = Brush.verticalGradient(
        listOf(
            Color(0xFF0F2027),
            Color(0xFF203A43),
            Color(0xFF2C5364)
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Visit Details") }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            InfoItem("Customer", visit.customerName ?: "N/A")
            InfoItem("Contact Person", visit.contactPerson ?: "N/A")
            InfoItem("Territory", visit.territory)
            InfoItem("Location", visit.location ?: "N/A")
            InfoItem("Visit Date", formatter.format(Date(visit.visitDate)))

            Spacer(modifier = Modifier.height(10.dp))

            InfoItem("Outcome", visit.outcomeStatus)
            InfoItem("Follow-up Date", visit.followUpDate ?: "N/A")

            Spacer(modifier = Modifier.height(10.dp))

            InfoItem("Notes", visit.rawNotes)
            InfoItem("Summary", visit.meetingSummary ?: "N/A")
            InfoItem("Pain Points", visit.painPoints ?: "N/A")
            InfoItem("Action Items", visit.actionItems ?: "N/A")
            InfoItem("Next Step", visit.nextStep ?: "N/A")

            Spacer(modifier = Modifier.height(10.dp))

            InfoItem("Emotion", visit.customerEmotion ?: "N/A")
            InfoItem("Deal Probability", visit.dealProbability ?: "N/A")
            InfoItem("Strategy", visit.suggestedStrategy ?: "N/A")
        }
    }
}

@Composable
fun InfoItem(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Text(label, color = Color.LightGray)
        Text(value, color = Color.White, style = MaterialTheme.typography.bodyLarge)
    }
}
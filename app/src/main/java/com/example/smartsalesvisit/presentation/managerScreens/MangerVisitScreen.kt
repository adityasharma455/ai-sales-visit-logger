package com.example.smartsalesvisit.presentation.managerScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.smartsalesvisit.domain.models.Visit
import com.example.smartsalesvisit.presentation.navigation.ManagerSummaryScreen
import com.example.smartsalesvisit.presentation.navigation.ManagerVisitDetailScreen
import org.koin.compose.viewmodel.koinViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ManagerVisitScreen(
    viewModel: ManagerViewModel = koinViewModel(),
    navController: NavController
) {

    val state = viewModel.getAllTerritoryVisitState.collectAsStateWithLifecycle()
    var territory = rememberSaveable { mutableStateOf("") }

    LaunchedEffect(true) {
        if (state.value.Success.isEmpty()) {
            viewModel.getAllTerritoryVisit(territory.value)
        }
    }

    val gradient = Brush.verticalGradient(
        listOf(
            Color(0xFF0F2027),
            Color(0xFF203A43),
            Color(0xFF2C5364)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(top = 30.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column {
                Text(
                    text = "👋 Welcome Manager",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )

                Text(
                    text = "Track and manage your team's performance",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }

            IconButton(
                onClick = {
                    navController.navigate(ManagerSummaryScreen)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Assessment,
                    contentDescription = "Summary",
                    tint = Color.Cyan
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.1f)
            ),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {

            OutlinedTextField(
                value = territory.value,
                onValueChange = { territory.value = it },
                label = {
                    Text(
                        "Search Territory",
                        color = Color.White.copy(alpha = 0.7f)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.7f)
                ),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            viewModel.getAllTerritoryVisit(territory.value)
                        }
                    ) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        StatsSection(state.value.Success)

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn {
            items(state.value.Success) { visit ->
                ManagerVisitItem(
                    visit = visit,
                    viewModel = viewModel,
                    onClick = {
                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set("visit", visit)

                        navController.navigate(ManagerVisitDetailScreen)
                    }
                )
            }
        }
    }
}

@Composable
fun StatsSection(visits: List<Visit>) {

    val total = visits.size
    val followUps = visits.count { it.outcomeStatus == "Follow-up needed" }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        StatCard("Total", total.toString(), Color(0xFF4CAF50))
        StatCard("Follow-ups", followUps.toString(), Color(0xFFFF9800))
    }
}

@Composable
fun StatCard(title: String, value: String, accent: Color) {

    Card(
        modifier = Modifier
            .width(110.dp)
            .height(90.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.12f)
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = title,
                color = Color.White.copy(alpha = 0.7f),
                style = MaterialTheme.typography.labelMedium
            )

            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                color = accent
            )
        }
    }
}

@Composable
fun ManagerVisitItem(
    visit: Visit,
    viewModel: ManagerViewModel,
    onClick: () -> Unit
) {

    val formatter = remember {
        SimpleDateFormat("dd MMM yyyy  HH:mm", Locale.getDefault())
    }

    val formattedDate = formatter.format(Date(visit.visitDate))

    val salesPersonMap = viewModel.salesPersonMap.collectAsStateWithLifecycle()
    val salesPerson = salesPersonMap.value[visit.salesPersonId]

    LaunchedEffect(visit.salesPersonId) {
        viewModel.getSalesPersonById(visit.salesPersonId)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.08f)
        ),
        onClick = onClick
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column(modifier = Modifier.weight(1f)) {

                    Text(
                        text = visit.customerName ?: "Unknown Customer",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = formattedDate,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.LightGray
                    )

                    salesPerson?.let {
                        Text(
                            text = "👤 ${it.name ?: "Unknown"}",
                            color = Color.White,
                            style = MaterialTheme.typography.bodySmall
                        )

                        Text(
                            text = "📞 ${it.phone ?: "N/A"}",
                            color = Color.White.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = when (visit.outcomeStatus) {
                            "Completed" -> Color(0xFF4CAF50)
                            "Follow-up needed" -> Color(0xFFFF9800)
                            else -> Color.Gray
                        }
                    )
                ) {
                    Text(
                        text = visit.outcomeStatus ?: "Unknown",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = "📍 ${visit.territory ?: "N/A"}",
                    color = Color.White.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = "😊 ${visit.customerEmotion ?: "N/A"}",
                    color = Color.White.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Deal Probability: ${visit.dealProbability ?: "N/A"}",
                color = Color(0xFF64B5F6),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Divider(color = Color.White.copy(alpha = 0.2f))

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = visit.meetingSummary ?: "No summary available",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3
            )
        }
    }
}
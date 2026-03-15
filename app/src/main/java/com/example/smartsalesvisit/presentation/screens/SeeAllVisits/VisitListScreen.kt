package com.example.smartsalesvisit.presentation.screens.SeeAllVisits

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartsalesvisit.domain.models.Visit
import org.koin.compose.viewmodel.koinViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VisitListScreen(
    viewModel: VisitViewModel = koinViewModel(),
    onVisitClick: (Visit) -> Unit,
    onAddVisitClick: () -> Unit
) {

    val state by viewModel.visitState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getAllVisits()
    }

    val gradient = Brush.verticalGradient(
        listOf(
            Color(0xFF0F2027),
            Color(0xFF203A43),
            Color(0xFF2C5364)
        )
    )

    Scaffold(

        topBar = {

            Column {

                TopAppBar(

                    title = {
                        Text(
                            text = "Customer Visits",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    },

                    actions = {

                        IconButton(
                            onClick = {
                                viewModel.refreshVisits(context)
                            }
                        ) {

                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh"
                            )

                        }

                    }

                )

                if (state.isRefreshing) {

                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp)
                    )

                }

            }

        },

        floatingActionButton = {

            FloatingActionButton(
                onClick = { onAddVisitClick() },
                containerColor = MaterialTheme.colorScheme.primary
            ) {

                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Visit"
                )

            }

        }

    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(padding)
        ) {

            if (state.Success.isEmpty() && state.isLoading != true) {

                EmptyVisitUI(
                    onAddVisitClick = onAddVisitClick
                )

            } else {

                LazyColumn(
                    contentPadding = PaddingValues(
                        bottom = 80.dp,
                        top = 10.dp
                    )
                ) {

                    items(state.Success) { visit ->

                        VisitItem(
                            visit = visit,
                            onClick = { onVisitClick(visit) }
                        )

                    }

                }

            }

            if (state.isLoading == true) {

                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White
                )

            }

        }

    }

}

@Composable
fun EmptyVisitUI(
    onAddVisitClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "No Visits Yet",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Create your First Visit and start tracking customer meetings.",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White.copy(alpha = 0.8f)
        )

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = { onAddVisitClick() }
        ) {

            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text("Create First Visit")

        }

    }

}

@Composable
fun VisitItem(
    visit: Visit,
    onClick: () -> Unit
) {

    val formatter = remember {
        SimpleDateFormat("dd MMM yyyy  HH:mm", Locale.getDefault())
    }

    val formattedDate = formatter.format(Date(visit.visitDate))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = onClick
    ) {

        Column {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = visit.customerName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = formattedDate,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )

                }

                AssistChip(
                    onClick = {},
                    label = {
                        Text(visit.syncStatus)
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )

            }

            Divider()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {

                Text(
                    text = "AI Summary",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = visit.meetingSummary
                        ?: "AI summary not generated yet.",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 5
                )

            }

        }

    }

}
//package com.example.smartsalesvisit.presentation.screens.SeeAllVisits
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Add
//import androidx.compose.material.icons.filled.Refresh
//import androidx.compose.material3.*
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.AssistChipDefaults
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import com.example.smartsalesvisit.domain.models.Visit
//import org.koin.compose.viewmodel.koinViewModel
//import java.text.SimpleDateFormat
//import java.util.*
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun VisitListScreen(
//    viewModel: VisitViewModel = koinViewModel(),
//    onVisitClick: (Visit) -> Unit,
//    onAddVisitClick: () -> Unit
//) {
//
//    val state by viewModel.visitState.collectAsStateWithLifecycle()
//    val context = LocalContext.current
//
//    LaunchedEffect(Unit) {
//        viewModel.getAllVisits()
//    }
//
//    Scaffold(
//
//        topBar = {
//
//            TopAppBar(
//
//                title = {
//                    Text(
//                        text = "Customer Visits",
//                        style = MaterialTheme.typography.titleLarge
//                    )
//                },
//
//                actions = {
//
//                    IconButton(
//                        onClick = {
//                            viewModel.refreshVisits(context)
//                        }
//                    ) {
//
//                        if (state.isRefreshing) {
//
//                            CircularProgressIndicator(
//                                modifier = Modifier.size(20.dp),
//                                strokeWidth = 2.dp
//                            )
//
//                        } else {
//
//                            Icon(
//                                imageVector = Icons.Default.Refresh,
//                                contentDescription = "Refresh"
//                            )
//
//                        }
//
//                    }
//
//                }
//
//            )
//
//        },
//
//        floatingActionButton = {
//
//            FloatingActionButton(
//                onClick = { onAddVisitClick() }
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Add,
//                    contentDescription = "Add Visit"
//                )
//            }
//
//        }
//
//    ) { padding ->
//
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding)
//        ) {
//
//            LazyColumn {
//
//                items(state.Success) { visit ->
//
//                    VisitItem(
//                        visit = visit,
//                        onClick = { onVisitClick(visit) }
//                    )
//
//                }
//
//            }
//
//            if (state.isLoading == true) {
//
//                CircularProgressIndicator(
//                    modifier = Modifier.align(Alignment.Center)
//                )
//
//            }
//
//        }
//
//    }
//
//}
//
//@Composable
//fun VisitItem(
//    visit: Visit,
//    onClick: () -> Unit
//) {
//
//    val formatter = remember {
//        SimpleDateFormat("dd MMM yyyy  HH:mm", Locale.getDefault())
//    }
//
//    val formattedDate = formatter.format(Date(visit.visitDate))
//
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 14.dp, vertical = 8.dp),
//        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.surface
//        ),
//        onClick = onClick
//    ) {
//
//        Column {
//
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//
//                Column(
//                    modifier = Modifier.weight(1f)
//                ) {
//
//                    Text(
//                        text = visit.customerName,
//                        style = MaterialTheme.typography.titleLarge
//                    )
//
//                    Spacer(modifier = Modifier.height(4.dp))
//
//                    Text(
//                        text = formattedDate,
//                        style = MaterialTheme.typography.bodySmall,
//                        color = MaterialTheme.colorScheme.primary
//                    )
//
//                }
//
//                AssistChip(
//                    onClick = {},
//                    label = {
//                        Text(visit.syncStatus)
//                    },
//                    colors = AssistChipDefaults.assistChipColors(
//                        containerColor = MaterialTheme.colorScheme.primaryContainer
//                    )
//                )
//
//            }
//
//            Divider()
//
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp)
//            ) {
//
//                Text(
//                    text = "AI Summary",
//                    style = MaterialTheme.typography.labelLarge,
//                    color = MaterialTheme.colorScheme.primary
//                )
//
//                Spacer(modifier = Modifier.height(6.dp))
//
//                Text(
//                    text = visit.meetingSummary
//                        ?: "AI summary not generated yet.",
//                    style = MaterialTheme.typography.bodyMedium,
//                    maxLines = 5
//                )
//
//            }
//
//        }
//
//    }
//
//}
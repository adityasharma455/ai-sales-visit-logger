package com.example.smartsalesvisit.presentation.screens.VistsByDate

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartsalesvisit.domain.models.Visit
import com.example.smartsalesvisit.presentation.managerScreens.StatCard
import com.example.smartsalesvisit.presentation.screens.utils.DatePickerField
import org.koin.compose.viewmodel.koinViewModel



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesAnalyticsScreen(
    onVisitClick: (Visit) -> Unit,
    viewModel: SalesAnalyticsViewModel = koinViewModel()
) {

    val state by viewModel.getVisitsByDateForCurrentUserState.collectAsStateWithLifecycle()

    var selectedDateMillis by remember { mutableStateOf<Long?>(null) }

    val gradient = Brush.verticalGradient(
        listOf(
            Color(0xFF141E30),
            Color(0xFF243B55)
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "📊 Sales Analytics",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(padding)
                .padding(16.dp)
        ) {

            // 🔥 FILTER CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.08f)
                )
            ) {

                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        "Filter by Date",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // ✅ FIXED DATE PICKER VISIBILITY
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.95f),
                        shadowElevation = 6.dp
                    ){
                        DatePickerField(
                            label = "Select Visit Date"
                        ) { millis, _ ->
                            selectedDateMillis = millis
                        }

                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            selectedDateMillis?.let {
                                viewModel.fetchVisitsByDate(it)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Search")
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            when {

                state.isLoading == true -> {
                    CircularProgressIndicator(color = Color.White)
                }

                state.Error != null -> {
                    Text(state.Error!!, color = Color.Red)
                }

                state.Success != null -> {

                    val visits = state.Success!!

                    if (visits.isEmpty()) {
                        Text("No visits found", color = Color.White)
                        return@Column
                    }

                    // 🔥 SORTING (MOST IMPORTANT LOGIC)
                    val sortedVisits = visits.sortedWith(
                        compareBy {
                            when (it.outcomeStatus) {
                                "Follow-up needed" -> 0
                                "Completed" -> 1
                                "No interest" -> 2
                                else -> 3
                            }
                        }
                    )

                    // 🔥 STATS
                    val total = visits.size
                    val follow = visits.count { it.outcomeStatus == "Follow-up needed" }
                    val completed = visits.count { it.outcomeStatus == "Completed" }
                    val notInterested = visits.count { it.outcomeStatus == "No interest" }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatCard("Total", total.toString(), Color.Green)
                        StatCard("Follow", follow.toString(), Color.Yellow)
                        StatCard("Done", completed.toString(), Color.Cyan)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    StatCard("Not Interested", notInterested.toString(), Color.Red)

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        "Visits (Priority Sorted)",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyColumn {

                        items(sortedVisits) { visit ->

                            val statusColor = when (visit.outcomeStatus) {
                                "Follow-up needed" -> Color.Yellow
                                "Completed" -> Color.Green
                                "No interest" -> Color.Red
                                else -> Color.LightGray
                            }

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                shape = RoundedCornerShape(14.dp),
                                onClick = {onVisitClick(visit)},
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White.copy(alpha = 0.06f)
                                )
                            ) {

                                Column(modifier = Modifier.padding(14.dp)) {

                                    Text(
                                        visit.customerName,
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        visit.outcomeStatus,
                                        color = statusColor
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
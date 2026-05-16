package com.example.smartsalesvisit.presentation.managerScreens.summaryScreen

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
import com.example.smartsalesvisit.presentation.screens.utils.DatePickerField
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerSummaryScreen(
    viewModel: ManagerSummaryViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    var selectedDate by remember { mutableStateOf<Long?>(null) }

    val gradient = Brush.verticalGradient(
        listOf(
            Color(0xFF0F2027),
            Color(0xFF203A43)
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "📊 Manager Dashboard",
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

            // 🔥 FILTER CARD (MATCHING YOUR OTHER SCREEN)
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

                    // ✅ SAME DATE PICKER STYLE
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.95f),
                        shadowElevation = 6.dp
                    ) {
                        DatePickerField(
                            label = "Select Date"
                        ) { millis, _ ->
                            selectedDate = millis
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            selectedDate?.let {
                                viewModel.fetchSummaryByDate(it)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Get Summary")
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            when {

                // 🔄 LOADING
                state.isLoading -> {
                    CircularProgressIndicator(color = Color.White)
                }

                // ❌ NO DATA
                state.error != null -> {
                    Text(
                        text = "⚠️ Till now no visits created for this date",
                        color = Color.Red
                    )
                }

                // ✅ SUCCESS
                state.summary != null -> {

                    val summary = state.summary!!

                    LazyColumn {

                        // 🔥 OVERALL SUMMARY
                        item {
                            Text(
                                "Overall Summary",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                summary.overallSummary,
                                color = Color.White
                            )
                        }

                        // 🔥 TOP TERRITORY
                        item {
                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                "Top Territory: ${summary.topTerritory}",
                                color = Color.Green,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // 🔥 TERRITORY PERFORMANCE LIST
                        items(summary.territorySummaries.sortedBy { it.rank }) { t ->

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White.copy(alpha = 0.08f)
                                )
                            ) {

                                Column(modifier = Modifier.padding(12.dp)) {

                                    Text(
                                        "${t.rank}. ${t.territory.uppercase()}",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        "Performance: ${t.performance}",
                                        color = Color.White
                                    )

                                    Text(
                                        "Visits: ${t.totalVisits}",
                                        color = Color.White
                                    )

                                    Text(
                                        "Deal Prob: ${t.avgDealProbability}%",
                                        color = Color.White
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        t.summary,
                                        color = Color.LightGray
                                    )
                                }
                            }
                        }

                        // 🔥 INSIGHTS
                        item {
                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                "Insights",
                                color = Color.Yellow,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            summary.insights.forEach {
                                Text("• $it", color = Color.White)
                            }
                        }

                        // 🔥 ACTIONS
                        item {
                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                "Actions",
                                color = Color.Cyan,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            summary.actions.forEach {
                                Text("• $it", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}
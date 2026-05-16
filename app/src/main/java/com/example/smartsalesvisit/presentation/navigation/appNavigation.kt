package com.example.smartsalesvisit.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.*
import com.example.smartsalesvisit.domain.models.Visit
import com.example.smartsalesvisit.presentation.managerScreens.ManagerVisitDetailScreen
import com.example.smartsalesvisit.presentation.managerScreens.ManagerVisitScreen
import com.example.smartsalesvisit.presentation.managerScreens.summaryScreen.ManagerSummaryScreen
import com.example.smartsalesvisit.presentation.screens.AddVisitScreen.RegisterVisitScreen
import com.example.smartsalesvisit.presentation.screens.AuthScreen.LoginScreen
import com.example.smartsalesvisit.presentation.screens.ChatBotScreen.ChatBotScreen
import com.example.smartsalesvisit.presentation.screens.SeeAllVisits.VisitListScreen
import com.example.smartsalesvisit.presentation.screens.UpdateVisits.UpdateVisitScreen
import com.example.smartsalesvisit.presentation.screens.VistsByDate.SalesAnalyticsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    isLoggedIn: Boolean
) {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val startDestination =
        if (isLoggedIn) VisitListScreen else LoginScreen

    // 🔥 Bottom Bar Items (ONLY 2)
    val bottomItems = listOf(
        BottomItem("Visits", Icons.Default.List, VisitListScreen),
        BottomItem("Analytics", Icons.Default.Analytics, SalesAnalyticsScreen)
    )

    val isChatScreen = currentDestination?.route == ChatBotScreenRoute::class.qualifiedName


    // 🔥 SHOW BAR ONLY ON THESE SCREENS
    val showBottomBar = currentDestination?.hierarchy?.any {
        it.route == VisitListScreen::class.qualifiedName ||
                it.route == SalesAnalyticsScreen::class.qualifiedName
    } == true

    Scaffold(

        bottomBar = {
            if (showBottomBar) {

                NavigationBar(
                    containerColor = Color(0xFF1E2A38) // dark premium
                ) {

                    bottomItems.forEach { item ->

                        val selected = currentDestination?.hierarchy?.any {
                            it.route == item.route::class.qualifiedName
                        } == true

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(VisitListScreen) // avoid stack
                                    launchSingleTop = true
                                }
                            },
                            icon = {
                                Icon(
                                    item.icon,
                                    contentDescription = item.title
                                )
                            },
                            label = {
                                Text(item.title)
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.Cyan,
                                selectedTextColor = Color.Cyan,
                                unselectedIconColor = Color.LightGray,
                                unselectedTextColor = Color.LightGray,
                                indicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            if (currentDestination?.route == VisitListScreen::class.qualifiedName) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(AddVisitScreen)
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                }
            }
        }

//    floatingActionButton = {
//
//        if (!isChatScreen) {   // ❌ Hide on chat screen
//
//            Column {
//
//                FloatingActionButton(
//                    onClick = {
//                        navController.navigate(ChatBotScreenRoute)
//                    },
//                    containerColor = Color(0xFF00C853)
//                ) {
//                    Text("🤖")
//                }
//
//                Spacer(modifier = Modifier.height(12.dp))
//
//                if (currentDestination?.route == VisitListScreen::class.qualifiedName) {
//                    FloatingActionButton(
//                        onClick = {
//                            navController.navigate(AddVisitScreen)
//                        }
//                    ) {
//                        Icon(Icons.Default.Add, contentDescription = null)
//                    }
//                }
//            }
//        }
//    }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {

            composable<LoginScreen> {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(VisitListScreen) {
                            popUpTo(LoginScreen) { inclusive = true }
                        }
                    }
                )
            }

            composable<VisitListScreen> {

                VisitListScreen(
                    onVisitClick = { visit ->

                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set("visit", visit)

                        navController.navigate(UpdateVisitScreen)
                    },

                    onAddVisitClick = {
                        navController.navigate(AddVisitScreen)
                    }
                )
            }

            composable<SalesAnalyticsScreen> {
                SalesAnalyticsScreen(
                    onVisitClick = { visit ->

                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set("visit", visit)

                        navController.navigate(UpdateVisitScreen)
                    }
                )
            }

            composable<ManagerSummaryScreen> {
                ManagerSummaryScreen()
            }

            composable<ManagerVisitDetailScreen> {

                val visit =
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.get<Visit>("visit")

                visit?.let {
                    ManagerVisitDetailScreen(visit = it)
                }
            }

            composable<ManagerVisitScreen> {
                ManagerVisitScreen(navController = navController)
            }

            composable<ChatBotScreenRoute> {
                ChatBotScreen()
            }

            composable<AddVisitScreen> {
                RegisterVisitScreen(
                    onVisitAdded = {
                        navController.popBackStack()
                    }
                )
            }

            composable<UpdateVisitScreen> {

                val visit =
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.get<Visit>("visit")

                visit?.let {
                    UpdateVisitScreen(
                        visit = it,
                        onVisitUpdated = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}


// 🔥 UPDATED DATA CLASS
data class BottomItem(
    val title: String,
    val icon: ImageVector,
    val route: Any
)
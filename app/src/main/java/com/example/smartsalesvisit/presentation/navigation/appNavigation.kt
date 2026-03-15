package com.example.smartsalesvisit.presentation.navigation


import androidx.compose.runtime.*
import androidx.navigation.compose.*
import com.example.smartsalesvisit.domain.models.Visit
import com.example.smartsalesvisit.presentation.screens.AddVisitScreen.RegisterVisitScreen
import com.example.smartsalesvisit.presentation.screens.AuthScreen.LoginScreen
import com.example.smartsalesvisit.presentation.screens.SeeAllVisits.VisitListScreen
import com.example.smartsalesvisit.presentation.screens.UpdateVisits.UpdateVisitScreen

@Composable
fun AppNavigation(
    isLoggedIn: Boolean
) {

    val navController = rememberNavController()

    val startDestination =
        if (isLoggedIn) VisitListScreen else LoginScreen

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
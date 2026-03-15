package com.example.smartsalesvisit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.smartsalesvisit.domain.models.Visit
import com.example.smartsalesvisit.presentation.navigation.AppNavigation
import com.example.smartsalesvisit.presentation.screens.AddVisitScreen.RegisterVisitScreen
import com.example.smartsalesvisit.presentation.screens.AuthScreen.SignUpScreen
import com.example.smartsalesvisit.presentation.screens.SeeAllVisits.VisitListScreen
import com.example.smartsalesvisit.presentation.screens.UpdateVisits.UpdateVisitScreen
import com.example.smartsalesvisit.ui.theme.SmartSalesVisitTheme
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val firebaseAuth: FirebaseAuth by inject ()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartSalesVisitTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val isLoggedIn = firebaseAuth.currentUser != null
                    AppNavigation(isLoggedIn)
                }
            }
        }
    }
}


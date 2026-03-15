package com.example.smartsalesvisit.presentation.screens.AuthScreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartsalesvisit.domain.models.SalesPerson
import org.koin.compose.viewmodel.koinViewModel
import java.util.UUID

@Composable
fun SignUpScreen(
    viewModel: authViewModel = koinViewModel(),
    onLoginClick: () -> Unit = {}
) {

    val state by viewModel.signUpState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var territory by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(state.Success, state.error) {

        if (state.Success == true) {

            Toast.makeText(context,"Registration Successful",Toast.LENGTH_LONG).show()

            name = ""
            email = ""
            password = ""
            phone = ""
            territory = ""
        }

        if (state.error != null) {
            Toast.makeText(context,state.error,Toast.LENGTH_LONG).show()
        }

    }

    val gradient = Brush.verticalGradient(
        listOf(
            Color(0xFF0F2027),
            Color(0xFF203A43),
            Color(0xFF2C5364)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ){

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(8.dp)
        ){

            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ){

                Text(
                    text = "Create Account",
                    style = MaterialTheme.typography.headlineSmall
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = territory,
                    onValueChange = { territory = it },
                    label = { Text("Territory") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {

                        if(name.isBlank()){
                            Toast.makeText(context,"Enter Name",Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        if(email.isBlank()){
                            Toast.makeText(context,"Enter Email",Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        if(password.isBlank()){
                            Toast.makeText(context,"Enter Password",Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        val salesPerson = SalesPerson(
                            id = UUID.randomUUID().toString(),
                            name = name,
                            email = email,
                            password = password,
                            phone = phone,
                            territory = territory,
                            createdAt = System.currentTimeMillis()
                        )

                        viewModel.registerSalesPerson(salesPerson)

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ){

                    if(state.isLoading == true){
                        CircularProgressIndicator()
                    }else{
                        Text("Sign Up")
                    }

                }

                Spacer(Modifier.height(10.dp))

                Text(
                    text = "Already have an account? Login",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onLoginClick() },
                    color = MaterialTheme.colorScheme.primary
                )

            }

        }

    }

}
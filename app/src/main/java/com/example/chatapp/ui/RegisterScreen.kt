package com.example.chatapp.ui

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatapp.R
import com.example.chatapp.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    goToLogin: () -> Unit,
    goToHome: () -> Unit
) {

    val viewModel = AuthViewModel()

    var name by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    Column(

        modifier =
            Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.background
                )
                .padding(16.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Top

    ) {

        Spacer(
            modifier =
                Modifier.height(40.dp)
        )

        Image(

            painter =
                painterResource(
                    id =
                        R.mipmap.ic_launcher_foreground
                ),

            contentDescription = null,

            modifier =
                Modifier.size(150.dp)
        )

        Spacer(
            modifier =
                Modifier.height(12.dp)
        )

        Text(

            text = "NexaChat",

            fontSize = 32.sp,

            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier =
                Modifier.height(8.dp)
        )

        Text("Create account")

        Spacer(
            modifier =
                Modifier.height(32.dp)
        )

        OutlinedTextField(

            value = name,

            onValueChange = {
                name = it
            },

            modifier =
                Modifier.fillMaxWidth(),

            label = {
                Text("Name")
            },

            shape =
                RoundedCornerShape(16.dp)
        )

        Spacer(
            modifier =
                Modifier.height(16.dp)
        )

        OutlinedTextField(

            value = email,

            onValueChange = {
                email = it
            },

            modifier =
                Modifier.fillMaxWidth(),

            label = {
                Text("Email")
            },

            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        KeyboardType.Email
                ),

            shape =
                RoundedCornerShape(16.dp)
        )

        Spacer(
            modifier =
                Modifier.height(16.dp)
        )

        OutlinedTextField(

            value = password,

            onValueChange = {
                password = it
            },

            modifier =
                Modifier.fillMaxWidth(),

            label = {
                Text("Password")
            },

            visualTransformation =
                PasswordVisualTransformation(),

            shape =
                RoundedCornerShape(16.dp)
        )

        Spacer(
            modifier =
                Modifier.height(24.dp)
        )

        Button(

            onClick = {

                errorMessage = ""

                if(
                    name.isBlank() ||
                    email.isBlank() ||
                    password.isBlank()
                ){

                    errorMessage =
                        "Enter all fields"

                    return@Button
                }

                if(
                    !Patterns.EMAIL_ADDRESS
                        .matcher(email)
                        .matches()
                ){

                    errorMessage =
                        "Invalid email format"

                    return@Button
                }

                viewModel.register(
                    name,
                    email,
                    password
                ) { success ->

                    if(success){
                        goToHome()
                    }
                    else{
                        errorMessage =
                            "Email already exists"
                    }
                }
            },

            modifier =
                Modifier.fillMaxWidth(),

            shape =
                RoundedCornerShape(16.dp),

            colors =
                ButtonDefaults.buttonColors(
                    containerColor =
                        MaterialTheme.colorScheme.primary
                )

        ) {
            Text("Register")
        }

        if(errorMessage.isNotBlank()){

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Text(

                text = errorMessage,

                color =
                    MaterialTheme.colorScheme.error
            )
        }

        Spacer(
            modifier =
                Modifier.height(12.dp)
        )

        TextButton(
            onClick = {
                goToLogin()
            }
        ) {
            Text(
                "Already have account?"
            )
        }
    }
}
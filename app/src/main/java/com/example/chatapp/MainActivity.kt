package com.example.chatapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.example.chatapp.ui.NavGraph
import com.example.chatapp.ui.theme.ChatAppTheme
import com.example.chatapp.viewmodel.AuthViewModel
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        AuthViewModel().updateStatus(true)

        setContent {
            ChatAppTheme {
                ChatApp()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        AuthViewModel().updateStatus(false)
    }
}

@Composable
fun ChatApp() {
    Surface(color = MaterialTheme.colorScheme.background) {
        NavGraph()
    }
}
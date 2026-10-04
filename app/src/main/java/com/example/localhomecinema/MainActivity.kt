package com.example.localhomecinema

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LocalCinemaApp()
        }
    }
}


@Composable
fun LocalCinemaApp() {

    var serverRunning by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Text(
            text = "Local Cinema",
            style = MaterialTheme.typography.headlineLarge
        )


        Spacer(
            modifier = Modifier.height(40.dp)
        )


        Text(
            text =
                if(serverRunning)
                    "🟢 Server Running"
                else
                    "🔴 Server Stopped",

            style = MaterialTheme.typography.titleMedium
        )


        Spacer(
            modifier = Modifier.height(30.dp)
        )


        Button(
            onClick = {
                serverRunning = !serverRunning
            }
        ) {

            Text(
                if(serverRunning)
                    "Stop Server"
                else
                    "Start Server"
            )
        }


        Spacer(
            modifier = Modifier.height(40.dp)
        )


        Text(
            text = "Current Movie:",
            style = MaterialTheme.typography.titleMedium
        )


        Text(
            text = "No Movie Selected"
        )


        Spacer(
            modifier = Modifier.height(20.dp)
        )


        Button(
            onClick = {

            }
        ) {

            Text("Select Movie")
        }


    }
}
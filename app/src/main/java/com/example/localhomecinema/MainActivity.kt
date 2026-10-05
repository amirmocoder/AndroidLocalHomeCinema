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
import com.example.localhomecinema.data.Movie
import com.example.localhomecinema.server.CinemaServer
import com.example.localhomecinema.server.CinemaState
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.activity.compose.rememberLauncherForActivityResult
import android.provider.OpenableColumns

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

    val context = LocalContext.current

    var serverRunning by remember {
        mutableStateOf(false)
    }

    var cinemaServer: CinemaServer? by remember {
        mutableStateOf(null)
    }

    var currentMovie by remember {

        mutableStateOf(
            Movie(
                title = "No Movie",
                description = "No movie selected"
            )
        )
    }

    val videoPicker = rememberLauncherForActivityResult(

        contract = ActivityResultContracts.GetContent()

    ) { uri: Uri? ->


        uri?.let {


            val cursor =
                context.contentResolver.query(
                    it,
                    null,
                    null,
                    null,
                    null
                )


            var size: Long = 0


            cursor?.use { c ->

                val sizeIndex =
                    c.getColumnIndex(
                        android.provider.OpenableColumns.SIZE
                    )


                if(sizeIndex >= 0 && c.moveToFirst()){

                    size =
                        c.getLong(sizeIndex)

                }

            }



            CinemaState.currentMovie.videoSource =
                it.toString()



            CinemaState.currentMovie.videoSize =
                size


            println(
                "VIDEO SIZE = $size bytes"
            )


        }

    }


    val posterPicker = rememberLauncherForActivityResult(

        contract = ActivityResultContracts.GetContent()

    ) { uri: Uri? ->


        uri?.let {


            CinemaState.currentMovie.posterPath =
                it.toString()


        }

    }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        horizontalAlignment = Alignment.CenterHorizontally

    ) {



        Text(

            text = "🎬 Local Cinema",

            style = MaterialTheme.typography.headlineLarge

        )


        Spacer(
            modifier = Modifier.height(30.dp)
        )



        Text(

            text =
                if(serverRunning)

                    "🟢 Server Running"

                else

                    "🔴 Server Stopped"

        )



        Spacer(
            modifier = Modifier.height(20.dp)
        )



        Button(

            onClick = {


                if(serverRunning){

                    cinemaServer?.stop()

                    cinemaServer = null

                    serverRunning = false

                }

                else{

                    val server = CinemaServer(
                        context = context,
                        port = 8080
                    )

                    server.start()


                    cinemaServer = server


                    serverRunning = true

                }

            }

        ){

            Text(

                if(serverRunning)

                    "Stop Server"

                else

                    "Start Server"

            )

        }

        Button(

            onClick = {


                CinemaState.isPlaying = true


                CinemaState.startTime =
                    System.currentTimeMillis()


            }

        ){

            Text("▶ Start Cinema")

        }


        Spacer(
            modifier = Modifier.height(40.dp)
        )



        Text(

            text = "Current Movie",

            style = MaterialTheme.typography.titleMedium

        )


        Spacer(
            modifier = Modifier.height(10.dp)
        )


        Text(
            text = currentMovie.title
        )


        Text(
            text = currentMovie.description
        )



        Spacer(
            modifier = Modifier.height(30.dp)
        )



        Button(

            onClick = {


                val movie = Movie(

                    title = "Interstellar",

                    description =
                        "A science fiction movie example"

                )


                currentMovie = movie


                CinemaState.currentMovie = movie


            }

        ){

            Text("Load Test Movie")

        }

        Button(

            onClick = {

                posterPicker.launch("image/*")

            }

        ){

            Text("Select Poster")

        }

        Button(

            onClick = {

                videoPicker.launch("video/*")

            }

        ){

            Text("Select Video")

        }
    }

}
package com.example.localhomecinema.server

import com.example.localhomecinema.data.Movie

object CinemaState {


    var currentMovie = Movie(
        title = "No Movie",
        description = "No movie selected"
    )


    var isPlaying = false

    var startTime: Long = 0


}
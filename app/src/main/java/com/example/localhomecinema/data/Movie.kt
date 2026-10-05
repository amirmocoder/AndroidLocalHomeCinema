package com.example.localhomecinema.data


data class Movie(

    var title: String = "",

    var description: String = "",

    var posterPath: String = "",

    var videoSource: String = "",

    var videoSize: Long = 0,

    var startTime: Long = 0

)
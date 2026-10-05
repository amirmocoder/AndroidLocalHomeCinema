package com.example.localhomecinema.server

import android.content.Context
import android.net.Uri
import fi.iki.elonen.NanoHTTPD
import java.net.URLDecoder
import android.os.ParcelFileDescriptor
import java.io.FileInputStream
import java.io.InputStream

class CinemaServer(
    private val context: Context,
    port:Int = 8080
): NanoHTTPD(port) {


    override fun serve(session: IHTTPSession): Response {

        val rangeHeader =
            session.headers["range"]

        if(session.uri == "/video") {


            val movie =
                CinemaState.currentMovie


            val uri =
                Uri.parse(movie.videoSource)


            val pfd =
                context.contentResolver
                    .openFileDescriptor(uri, "r")


            if(pfd != null){


                val fileSize =
                    movie.videoSize



                val range =
                    session.headers["range"]



                var start: Long = 0

                var end =
                    fileSize - 1



                if(range != null){


                    val values =
                        range.replace("bytes=", "")
                            .split("-")



                    start =
                        values[0].toLong()



                    if(values.size > 1 &&
                        values[1].isNotEmpty()){

                        end =
                            values[1].toLong()

                    }

                }

                val contentLength =
                    end - start + 1



                val input =
                    FileInputStream(
                        pfd.fileDescriptor
                    )


                input.skip(start)



                val response =
                    newFixedLengthResponse(

                        Response.Status.PARTIAL_CONTENT,

                        "video/mp4",

                        input,

                        contentLength

                    )



                response.addHeader(
                    "Accept-Ranges",
                    "bytes"
                )


                response.addHeader(
                    "Content-Range",
                    "bytes $start-$end/$fileSize"
                )


                response.addHeader(
                    "Content-Length",
                    contentLength.toString()
                )


                return response


            }

        }

        val movie = CinemaState.currentMovie

        if(session.uri == "/poster"){


            val posterPath =
                CinemaState.currentMovie.posterPath


            if(posterPath.isNotEmpty()){


                val uri =
                    Uri.parse(posterPath)


                val stream =
                    context.contentResolver.openInputStream(uri)



                if(stream != null){


                    return newChunkedResponse(

                        Response.Status.OK,

                        "image/jpeg",

                        stream

                    )

                }

            }


            return newFixedLengthResponse(

                Response.Status.NOT_FOUND,

                "text/plain",

                "Poster not found"

            )

        }

        if(session.uri == "/send"){


            val params = session.parameters


            val name =
                params["name"]?.firstOrNull()
                    ?: "Anonymous"


            val msg =
                params["msg"]?.firstOrNull()
                    ?: ""



            if(msg.isNotEmpty()){


                ChatManager.addMessage(
                    "$name: $msg"
                )


            }


            return newFixedLengthResponse(
                Response.Status.OK,
                "text/plain",
                "OK"
            )


        }

        if(session.uri == "/messages"){


            val html =

                ChatManager
                    .getMessages()
                    .joinToString("<br>")



            return newFixedLengthResponse(

                Response.Status.OK,

                "text/html",

                html

            )


        }

        val startTime = CinemaState.startTime

        val isPlaying = CinemaState.isPlaying

        val html = """

           <html>
                <head>
                    <style>
                    
                        body{
                        
                        background:#111;
                        color:white;
                        font-family:Arial;
                        text-align:center;
                        
                        }
                        
                        
                        .card{
                        
                        max-width:700px;
                        margin:auto;
                        
                        }
                        
                        
                        video{
                        
                        width:90%;
                        border-radius:10px;
                        
                        }
                        
                        
                        .poster{
                        
                        width:200px;
                        border-radius:10px;
                        
                        }
                
                    </style>
                
                </head>
                
                <body>
                
                    <div class="card">
                
                
                        <h1>
                        🎬 Local Cinema
                        </h1>
                    
                    
                        <img 
                        class="poster"
                        src="/poster"
                        />
                    
                    
                        <h2>
                        
                            ${movie.title}
                        
                        </h2>
                    
                    
                        <p>
                        
                        ${movie.description}
                        
                        </p>
                    
                    
                        <video id="player" controls>
                    
                            <source 
                            src="/video"
                            type="video/mp4">
                    
                        </video>
                        
                        <hr>


                        <div id="chat">
                        
                            <h2>
                                💬 Chat
                            </h2>
                        
                        <div id="messages"></div>
                        
                            <input 
                            id="username"
                            placeholder="Your name">
                                    
                            <br><br>
                        
                            <input 
                            id="message"
                            placeholder="Message">
                        
                    
                            <button onclick="sendMessage()">
                                Send
                            </button>
                        
                        </div>

                        <h3>
                        
                            Status:
                            
                            ${if(CinemaState.isPlaying)
                            "🟢 Playing"
                            else
                            "🔴 Ready"}
                        
                        </h3>
                     </div>
                     
                    <script>
                     
                        function loadMessages(){
                        
                        
                        fetch('/messages')
                        
                        .then(response => response.text())
                        
                        .then(data => {
                        
                        
                        document.getElementById("messages").innerHTML=data;
                        
                        
                        });
                        
                        
                        }
                        
                        
                        
                        function sendMessage(){
                        
                        
                        let name =
                        document.getElementById("username").value;
                        
                        
                        let msg =
                        document.getElementById("message").value;
                        
                        
                        
                        fetch(
                        '/send?name='
                        +encodeURIComponent(name)
                        +'&msg='
                        +encodeURIComponent(msg)
                        );
                        
                        
                        
                        document.getElementById("message").value="";
                        
                        
                        loadMessages();
                        
                        
                        }
                        
                        
                        
                        setInterval(
                        loadMessages,
                        3000
                        );
                        
                        
                        loadMessages();
                        
                        
                        let startTime = ${CinemaState.startTime};
                        
                        let playing =
                            ${CinemaState.isPlaying};
                        
                        let video = document.querySelector("video");

                            if(playing){
                            
                            let now = Date.now();

                            let offset = (now - startTime) / 1000;
                            
                            video.currentTime = offset;
                   
                            const cinemaStartTime = $startTime;
                            
                            const cinemaPlaying = $isPlaying;

                        }                       

                        window.onload = function(){
                            let video =
                            document.getElementById("player");
                            if(cinemaPlaying){
                                                  
                                let elapsed =
                                (Date.now() - cinemaStartTime) / 1000;
                                                       
                                if(elapsed > 0){
                            
                            
                                    video.currentTime = elapsed;
                                }
                            }
                        }
                            
                    </script>    
                </body>  
           </html>
    """

        if(session.uri == "/video"){


            val uriString =
                CinemaState.currentMovie.videoSource


            if(uriString.isNotEmpty()){


                val uri = Uri.parse(uriString)


                val inputStream =
                    context.contentResolver.openInputStream(uri)



                return newChunkedResponse(

                    Response.Status.OK,

                    "video/mp4",

                    inputStream

                )


            }


        }
        return newFixedLengthResponse(

            Response.Status.OK,

            "text/html",

            html

        )

    }

}
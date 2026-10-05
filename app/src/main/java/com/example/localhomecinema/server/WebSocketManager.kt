package com.example.localhomecinema.server

import fi.iki.elonen.NanoWSD.WebSocket


object WebSocketManager {


    private val clients =
        mutableListOf<WebSocket>()


    fun add(socket: WebSocket){

        clients.add(socket)

    }


    fun remove(socket: WebSocket){

        clients.remove(socket)

    }


    fun broadcast(message:String){

        clients.forEach {

            try {

                it.send(message)

            } catch(e:Exception){

                e.printStackTrace()

            }

        }

    }

}
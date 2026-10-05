package com.example.localhomecinema.server

import fi.iki.elonen.NanoHTTPD
import fi.iki.elonen.NanoWSD


class ChatWebSocketServer(
    port: Int
) : NanoWSD(port) {


    override fun openWebSocket(
        handshake: NanoHTTPD.IHTTPSession
    ): NanoWSD.WebSocket {


        return CinemaWebSocket(
            handshake
        )

    }


}
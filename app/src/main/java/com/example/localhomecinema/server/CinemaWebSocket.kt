package com.example.localhomecinema.server

import fi.iki.elonen.NanoHTTPD
import fi.iki.elonen.NanoWSD
import java.io.IOException


class CinemaWebSocket(
    handshakeRequest: NanoHTTPD.IHTTPSession
) : NanoWSD.WebSocket(handshakeRequest) {


    override fun onOpen() {

        WebSocketManager.add(this)

    }


    override fun onClose(
        code: NanoWSD.WebSocketFrame.CloseCode?,
        reason: String?,
        initiatedByRemote: Boolean
    ) {

        WebSocketManager.remove(this)

    }


    override fun onMessage(
        message: NanoWSD.WebSocketFrame
    ) {

        WebSocketManager.broadcast(
            message.textPayload
        )

    }


    override fun onPong(
        pong: NanoWSD.WebSocketFrame
    ) {

    }


    override fun onException(
        exception: IOException
    ) {

        exception.printStackTrace()

    }

}
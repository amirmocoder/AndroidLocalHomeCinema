package com.example.localhomecinema.server

import fi.iki.elonen.NanoHTTPD
import java.io.InputStream


class VideoRangeResponse(
    private val inputStream: InputStream,
    private val mimeType: String,
    private val contentLength: Long
) : NanoHTTPD.Response(
    Status.PARTIAL_CONTENT,
    mimeType,
    inputStream,
    contentLength
)
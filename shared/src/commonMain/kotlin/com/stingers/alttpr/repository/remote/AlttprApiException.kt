package com.stingers.alttpr.repository.remote

class AlttprApiException(val statusCode: Int, val rawBody: String) :
    Exception("HTTP $statusCode: $rawBody")

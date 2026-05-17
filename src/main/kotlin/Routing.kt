package com.streaming

import com.streaming.utils.HealthResponse
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/videos") {

        }

        get("/health") {
            call.respond(HealthResponse("UP"))
        }
    }
}
package com.streaming

import com.streaming.utils.HealthResponse
import com.streaming.utils.VideoItem
import com.streaming.utils.generateThumbnail
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun Application.configureRouting() {
    routing {
        get("/videos") {
            val videoDir = File(System.getenv("VIDEOS_DIR"))
            if (videoDir.exists() && videoDir.isDirectory){
                val videoList = videoDir.listFiles()
                ?.filter{ it .isFile && it.name.endsWith(".mp4") || it.name.endsWith(".ts")}
                    ?.map { file ->
                        val filename = file.name
                        val title = file.nameWithoutExtension

                        VideoItem(
                            id = filename,
                            title = title.replace("_", " "),
                            streamUrl = "/stream/$filename",
                            thumbnailUrl = "/thumb/$title.jpg"
                        )
                    } ?: emptyList()

                call.respond(videoList  )
            } else{
                call.respond(HttpStatusCode.NotFound)
            }
        }
        get("/stream/{fileName}") {
            /*TODO make the stream function*/
        }

        get("/thumb/{thumbName}"){
            val thumbName = call.parameters["thumbName"] ?: return@get call.respond(HttpStatusCode.BadRequest)

            val thumbFile = File("/app/thumb/$thumbName")
            val title = thumbFile.nameWithoutExtension

            val possibleVideoFiles = listOf(
                File("/app/videos/$title.mp4"),
                File("/app/videos/$title.ts")
            )
            val videoFile = possibleVideoFiles.find { it.exists() }

            if (thumbFile.exists()){
                call.respond(thumbFile)
            } else if (videoFile != null){
                val success = generateThumbnail(videoFile = videoFile, thumbFile = thumbFile)
                if (success){
                    call.respond(thumbFile)
                } else {
                    call.respond(HttpStatusCode.InternalServerError, "Failed to generate thumbnail")
                }
            } else {
                call.respond(HttpStatusCode.NotFound, "File not found")
            }

        }

        get("/health") {
            call.respond(HealthResponse("UP"))
        }
    }
}
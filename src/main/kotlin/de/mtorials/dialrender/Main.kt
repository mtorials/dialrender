package de.mtorials.dialrender

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import de.mtorials.dialphone.DialPhone
import de.mtorials.dialphone.entities.Message
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.lens.Path
import org.http4k.routing.ResourceLoader.Companion.Directory
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static
import org.http4k.server.Jetty
import org.http4k.server.asServer
import org.http4k.template.HandlebarsTemplates
import org.http4k.template.ViewModel
import java.io.File

class Config(
    val homeserverUrl: String,
    val token: String
)

val config : Config = jacksonObjectMapper().readValue(File("config.json"))

fun main() {
    val messagesByPath: MutableMap<String, MutableList<Message>> = mutableMapOf()

    val phone = DialPhone {
        homeserverUrl = config.homeserverUrl
        withToken(config.token)
        addListeners {
            add(ReaderListener(messagesByPath))
            //add(RenderingStateListener(pathToRoomId))
            add(InitListener())
        }
        addCustomEventTypes {
            add(RenderingState::class)
        }
    }
    phone.sync()

    val roomLens = Path.of("room")
    val renderer = HandlebarsTemplates().HotReload("src/main/resources")

    val routes = routes(
        "/" bind Method.GET to handler@{
            val model = Index(
                otherFeedsName = messagesByPath.keys.toList()
            )
            Response(Status.OK).body(renderer(model))
        },
        "/ping" bind Method.GET to { Response(Status.OK).body("pong") },
        "/static" bind static(Directory("src/main/resources/static")),
        "/{room:.*}" bind Method.GET to handler@{ request ->
            val path = roomLens(request)
            if (!messagesByPath.containsKey(path)) {
                return@handler Response(Status.NOT_FOUND).body("Feed Not Found!")
            }
            val list = messagesByPath[path]?.reversed() ?: mutableListOf()
            val model = Feed(
                feedName = path,
                posts = list,
                otherFeedsName = messagesByPath.keys.toList()
            )
            Response(Status.OK).body(renderer(model))
        }
    )
    routes.asServer(Jetty(9099)).start()
}
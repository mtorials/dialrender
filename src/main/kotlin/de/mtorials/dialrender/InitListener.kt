package de.mtorials.dialrender

import de.mtorials.dialphone.dialevents.MessageReceivedEvent
import de.mtorials.dialphone.dialevents.answer
import de.mtorials.dialphone.listener.CommandAdapter
import kotlinx.coroutines.delay

class InitListener: CommandAdapter("init") {
    override suspend fun execute(event: MessageReceivedEvent, parameters: Array<String>) {
        println("Drin!")
        if (parameters.isEmpty()) {
            event answer "Missing path!"
        }
        event.roomFuture.sendStateEvent(RenderingState.Content(parameters[0]))
        delay(10000)
        event.message.redact()
    }
}
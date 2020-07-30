package de.mtorials.dialrender

import de.mtorials.dialphone.dialevents.MessageReceivedEvent
import de.mtorials.dialphone.entities.Message
import de.mtorials.dialphone.listener.ListenerAdapter

class ReaderListener(
    private val messagesByPath: MutableMap<String, MutableList<Message>>
) : ListenerAdapter(true) {
    override suspend fun onRoomMessageReceive(event: MessageReceivedEvent) {
        if (event.sender.id == event.phone.ownId) return
        val renderingState = event.roomFuture.receiveStateEvents().filterIsInstance<RenderingState>()
        if (renderingState.isEmpty()) return
        val path = renderingState[0].content.path
        if (!messagesByPath.containsKey(path)) {
            messagesByPath[path] = mutableListOf()
        }
        messagesByPath[path]?.add(event.message)
    }
}
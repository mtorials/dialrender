package de.mtorials.dialrender

import de.mtorials.dialphone.entities.entityfutures.RoomFutureImpl
import de.mtorials.dialphone.listener.MatrixEventAdapter

class RenderingStateListener(
    private val pathToRoomId: MutableMap<String, String>
): MatrixEventAdapter<RenderingState>(RenderingState::class, true) {
    override fun onMatrixEvent(event: RenderingState, roomFuture: RoomFutureImpl) {
        pathToRoomId.forEach { (path, roomId) ->
            if (roomId == roomFuture.id) pathToRoomId.remove(path)
        }
        pathToRoomId[event.content.path] = roomFuture.id
    }
}
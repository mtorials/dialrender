package de.mtorials.dialrender

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeName
import de.mtorials.dialphone.ContentEventType
import de.mtorials.dialphone.mevents.roomstate.MatrixStateEvent
import de.mtorials.dialphone.mevents.roomstate.StateEventContent

@JsonTypeName("de.mtorials.matrix.testevents.renderingstate")
class RenderingState(
    override val sender: String,
    @JsonProperty("state_key")
    override val stateKey: String,
    @JsonProperty("event_id")
    override val id: String,
    @JsonProperty("prev_content")
    override val prevContent: Content?,
    override val content: Content
) : MatrixStateEvent {
    @ContentEventType(RenderingState::class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Content(
        val path: String
    ) : StateEventContent
}
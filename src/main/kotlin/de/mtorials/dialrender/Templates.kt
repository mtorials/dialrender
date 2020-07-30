package de.mtorials.dialrender

import de.mtorials.dialphone.entities.Message
import org.http4k.template.ViewModel

data class Feed(
    val posts: List<Message>,
    val feedName: String,
    val otherFeedsName: List<String>
) : ViewModel {
    override fun template(): String = "Feed"
}

data class Index(
    val otherFeedsName: List<String>
) : ViewModel {
    override fun template(): String = "Index"
}
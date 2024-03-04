package common.event.serverevents

import common.Config
import common.event.ServerEvent
import common.math.Float3

class UpdatePositionRequestEvent(position: Float3): ServerEvent() {
    // Because Float3 wasn't serialising properly :(
    val x = position.x
    val y = position.y
    val z = position.z

    override fun event() {
        val position = Float3(x, y, z)

        val chunkPosition = (position / Config.chunkSize).toInt3()
        // println(chunkPosition)
        player!!.position = position
    }
}
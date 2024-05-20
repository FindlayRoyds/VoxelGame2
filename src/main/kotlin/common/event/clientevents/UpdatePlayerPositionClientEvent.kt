package common.event.clientevents

import common.event.ClientEvent
import common.math.Double3

class UpdatePlayerPositionClientEvent(val playerId: Int, position: Double3): ClientEvent() {
    val x = position.x
    val y = position.y
    val z = position.z

    override fun event() {
        val position = Double3(x, y, z)
        val player = client.players.getPlayer(playerId)
        player?.position = position
    }
}
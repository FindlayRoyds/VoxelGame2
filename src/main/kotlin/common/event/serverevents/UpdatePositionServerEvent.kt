package common.event.serverevents

import common.Config
import common.event.ServerEvent
import common.event.clientevents.UpdatePlayerPositionClientEvent
import common.math.Double3

class UpdatePositionServerEvent(position: Double3): ServerEvent() {
    // Because Float3 wasn't serialising properly :(
    val x = position.x
    val y = position.y
    val z = position.z

    override fun event() {
        val position = Double3(x, y, z)

        val chunkPosition = (position / Config.chunkSize).toInt3()
        // println(chunkPosition)
        player!!.position = position

        val updatePlayerPositionEvent = UpdatePlayerPositionClientEvent(player!!.userID, position)
        server!!.serverNetwork.sendEventToEveryone(updatePlayerPositionEvent)
    }
}
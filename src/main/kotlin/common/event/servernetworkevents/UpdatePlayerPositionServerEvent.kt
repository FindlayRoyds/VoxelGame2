package common.event.servernetworkevents

import common.Config
import common.event.ServerEvent
import common.event.clientevents.UpdatePlayerPositionClientEvent
import common.math.Double3

class UpdatePlayerPositionServerEvent(val position: Double3): ServerEvent() {
    // Because Float3 wasn't serialising properly :(

    override fun event() {
        val chunkPosition = (position / Config.chunkSize).floor()
        player!!.position = position

        val updatePlayerPositionEvent = UpdatePlayerPositionClientEvent(player!!.userID, position)
        server.serverNetwork.sendEventToEveryone(updatePlayerPositionEvent)
    }
}
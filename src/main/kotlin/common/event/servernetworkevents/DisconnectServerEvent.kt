package common.event.servernetworkevents

import common.GameEngineProvider
import common.event.ServerEvent
import common.event.clientevents.DisconnectClientEvent
import server.Server

class DisconnectServerEvent : ServerEvent() {
    override fun event() {
        val server = GameEngineProvider.gameEngine as Server
        val userID = server.serverNetwork.getUserIDFromSocket(socket!!)
        if (userID != null) {
            val disconnectEvent = DisconnectClientEvent()
            server.serverNetwork.sendEventToUserID(disconnectEvent, userID)

            server.serverNetwork.removeUserID(userID)
            val player = server.players.getPlayer(userID)
            if (player != null) {
                server.players.removePlayer(player)
            }
        }

        socket?.close()
    }
}
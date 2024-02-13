package common.event.commonevents

import common.GameEngineProvider
import common.event.Event
import server.Server

class DisconnectEvent : Event() {
    override fun event() {
        val gameEngine = GameEngineProvider.getGameEngine()
        if (gameEngine.isServer()) {
            val server = gameEngine as Server
            val userID = server.serverNetwork.getUserIDFromSocket(socket!!)
            server.serverNetwork.removeUserID(userID)
            val player = server.players.getPlayer(userID)
            if (player != null) {
                server.players.removePlayer(player)
            }
        } else {
            println("Disconnected from server")
        }
    }
}
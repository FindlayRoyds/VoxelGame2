package common.event.commonevents

import common.GameEngineProvider
import common.event.Event
import server.Server
import kotlin.system.exitProcess

class DisconnectEvent : Event() {
    override fun event() {
        val gameEngine = GameEngineProvider.getGameEngine()
        if (gameEngine.isServer()) {
            val server = gameEngine as Server
            val userID = server.serverNetwork.getUserIDFromSocket(socket!!)
            if (userID != null) {
                server.serverNetwork.removeUserID(userID)
                val player = server.players.getPlayer(userID)
                if (player != null) {
                    server.players.removePlayer(player)
                }
            }
            socket?.close()
        } else {
            println("Disconnected from server")
            exitProcess(0)
        }
    }
}
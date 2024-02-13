package common.event.serverevents

import common.GameEngineProvider
import common.event.ServerEvent
import common.event.clientevents.ConnectionResponseEvent
import common.player.Player
import server.Server

class ConnectionRequestEvent(private val username: String) : ServerEvent() {
    override fun event() {
        val server = GameEngineProvider.getGameEngine() as Server

        val newPlayerUserID = server.serverNetwork.getUserIDFromSocket(socket!!)
        val newPlayer = Player(newPlayerUserID, username)
        server.players.addPlayer(newPlayer)

        val responseEvent = ConnectionResponseEvent(true)
        server.serverNetwork.sendEventToUserID(responseEvent, newPlayerUserID)
    }
}
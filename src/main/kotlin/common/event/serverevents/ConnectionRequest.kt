package common.event.serverevents

import common.GameEngineProvider
import common.event.ServerEvent
import common.event.clientevents.ConnectionRequestResponse
import common.player.Player
import server.Server

class ConnectionRequest(private val username: String) : ServerEvent() {
    override fun event() {
        val server = GameEngineProvider.getGameEngine() as Server

        println("$username has joined the game")
        println("Socket: $socket. Player: $playerReceivedFrom")

        val newPlayerUserID = server.serverSocketHandler.getUserIDFromSocket(socket!!)
        val responseEvent = ConnectionRequestResponse(true)
        server.serverSocketHandler.sendEventToUserID(responseEvent, newPlayerUserID)

        val newPlayer = Player(newPlayerUserID, username)
        server.players.addPlayer(newPlayer)
    }
}
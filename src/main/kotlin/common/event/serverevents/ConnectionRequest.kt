package common.event.serverevents

import common.GameEngineProvider
import common.event.ServerEvent
import common.player.Player
import server.Server

class ConnectionRequest(private val username: String) : ServerEvent() {
    override fun event() {
        println("$username has joined the game")
        println("Socket: $socket. Player: $playerReceivedFrom")

        val server = GameEngineProvider.getGameEngine() as Server
        val newPlayerUserID = server.serverSocketHandler.getUserIDFromSocket(socket!!)
        val newPlayer = Player(newPlayerUserID, username)
        server.players.addPlayer(newPlayer)
    }
}
package common.event

import common.GameEngineProvider
import common.player.Player
import server.Server

abstract class ServerEvent : Event() {
    protected var playerReceivedFrom: Player? = null

    override fun run() {
        val gameEngine = GameEngineProvider.getGameEngine()
        if (gameEngine.isClient())
            throw Exception("Server event attempting to run on a client!")

        if (socket != null) {
            val server = GameEngineProvider.getGameEngine() as Server
            val userID = server.serverSocketHandler.getUserIDFromSocket(socket!!)
            playerReceivedFrom = server.players.getPlayer(userID)
        }

        event()
    }
}
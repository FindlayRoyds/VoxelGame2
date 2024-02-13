package common.event

import common.GameEngineProvider
import common.player.Player
import server.Server

abstract class ServerEvent : Event() {
    protected var playerReceivedFrom: Player? = null
    protected var server: Server? = null

    override fun run() {
        val gameEngine = GameEngineProvider.getGameEngine()
        if (gameEngine.isClient())
            throw Exception("Server event attempting to run on a client!")
        server = gameEngine as Server

        if (socket != null) {
            val server = GameEngineProvider.getGameEngine() as Server
            val userID = server.serverNetwork.getUserIDFromSocket(socket!!)
            playerReceivedFrom = server.players.getPlayer(userID)
        }

        event()
    }
}
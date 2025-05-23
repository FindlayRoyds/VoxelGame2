package common.event

import common.GameEngineProvider
import common.player.Player
import server.Server
import java.io.Serializable
import java.net.Socket

abstract class ServerEvent : Event(), Serializable {
    protected var player: Player? = null
    var socket: Socket? = null
    private var _server: Server? = null
    protected val server: Server
        get() {
            if (_server == null)
                _server = GameEngineProvider.gameEngine as Server
            return _server!!
        }

    override fun run() {
        if (socket != null) {
            val server = GameEngineProvider.gameEngine as Server
            val userID = server.serverNetwork.getUserIDFromSocket(socket!!)
            player = server.players.getPlayer(userID!!)
        }

        event()
    }
}
package common.event.servernetworkevents

import common.event.ServerEvent
import common.event.clientevents.SetupGameClientEvent
import common.player.Player

class ConnectionRequestServerEvent(private val username: String) : ServerEvent() {
    override fun event() {
        val newPlayerUserID = server.serverNetwork.getUserIDFromSocket(socket!!)!!
        val newPlayer = Player(newPlayerUserID, username)
        server.players.addPlayer(newPlayer)

        val setupGameEvent = SetupGameClientEvent(server.players, newPlayerUserID)
        server.serverNetwork.sendEventToPlayer(setupGameEvent, newPlayer)
    }
}
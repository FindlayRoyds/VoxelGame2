package common.event.clientevents

import common.event.ClientEvent

class PlayerLeftClientEvent(private val userID: Int) : ClientEvent() {
    override fun event() {
        val player = client!!.players.getPlayer(userID)
        println("${player!!.username} has left the game")
        client!!.players.removePlayer(player)
    }
}
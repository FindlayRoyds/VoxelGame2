package common.event.clientevents

import common.event.ClientEvent
import common.player.Player

class PlayerJoinedClientEvent(player: Player) : ClientEvent() {
    private val playerData = player.getTransferObject(false)

    override fun event() {
        val newPlayer = Player(playerData.userID, playerData.username)
        client.players.addPlayer(newPlayer)
        println("${newPlayer.username} has joined the game")
    }
}
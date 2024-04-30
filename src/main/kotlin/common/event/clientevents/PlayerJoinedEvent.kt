package common.event.clientevents

import common.event.ClientEvent
import common.networking.transferobjects.PlayerTransferObject
import common.player.Player

class PlayerJoinedEvent(player: Player) : ClientEvent() {
    private val playerData = PlayerTransferObject(player, false)

    override fun event() {
        val newPlayer = Player(playerData.userID, playerData.username)
        client!!.players.addPlayer(newPlayer)
        println("${newPlayer.username} has joined the game")
    }
}
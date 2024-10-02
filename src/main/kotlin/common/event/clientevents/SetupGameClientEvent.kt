package common.event.clientevents

import common.event.ClientEvent
import common.player.Player
import common.player.Players

class SetupGameClientEvent(players: Players, sendingToUserId: Int) : ClientEvent() {
    private val playerTransferObjects = mutableListOf<Player.PlayerTransferObject>()

    init {
        for (player in players.getPlayerList())
            playerTransferObjects.add(player.getTransferObject(player.userID == sendingToUserId))
    }

    override fun event() {
        for (playerTransferObject in playerTransferObjects) {
            val player = playerTransferObject.getPlayer()
            client.players.addPlayer(player)
            if (playerTransferObject.isLocalPlayer) {
                client.players.localPlayer = player
            }
        }
    }
}
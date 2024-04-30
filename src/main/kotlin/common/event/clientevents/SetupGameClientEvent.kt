package common.event.clientevents

import common.event.ClientEvent
import common.networking.transferobjects.PlayerTransferObject
import common.player.Players

class SetupGameClientEvent(players: Players, sendingToUserId: Int) : ClientEvent() {
    private val playerTransferObjects = mutableListOf<PlayerTransferObject>()

    init {
        for (player in players.getPlayerList())
            playerTransferObjects.add(PlayerTransferObject(player, player.userID == sendingToUserId))
    }

    override fun event() {
        for (playerTransferObject in playerTransferObjects) {
            val player = playerTransferObject.getPlayer()
            client!!.players.addPlayer(player)
            if (playerTransferObject.isLocalPlayer) {
                client!!.players.localPlayer = player
            }
        }

        println(client!!.players.getPlayerList())
    }
}
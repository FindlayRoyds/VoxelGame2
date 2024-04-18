package common.event.clientevents

import common.event.ClientEvent
import common.networking.transferobjects.PlayerTransferObject
import common.player.Players

class SetupGameEvent(players: Players) : ClientEvent() {
    private val playerTransferObjects = mutableListOf<PlayerTransferObject>()

    init {
        for (player in players.getPlayerList())
            playerTransferObjects.add(PlayerTransferObject(player))
    }

    override fun event() {
        for (playerTransferObject in playerTransferObjects)
            client!!.players.addPlayer(playerTransferObject.getPlayer())
    }
}
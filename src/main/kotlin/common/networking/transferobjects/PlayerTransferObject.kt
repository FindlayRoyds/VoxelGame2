package common.networking.transferobjects

import common.player.Player
import java.io.Serializable

class PlayerTransferObject(player: Player, val isLocalPlayer: Boolean) : Serializable {
    val userID = player.userID
    val username = player.username

    fun getPlayer(): Player {
        return Player(userID, username)
    }
}
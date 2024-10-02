package common.player

import common.math.Double3
import common.math.Int3
import java.io.Serializable

class Player(var userID: Int, var username: String) {
    var position = Double3(0, 0, 0)

    var smoothedPosition = Double3(0, 0, 0)

    val loadedChunks = HashSet<Int3>()

    fun getTransferObject(isLocalPlayer: Boolean) = PlayerTransferObject(this, isLocalPlayer)

    class PlayerTransferObject(player: Player, val isLocalPlayer: Boolean) : Serializable {
        val userID = player.userID
        val username = player.username
        val position = player.position

        fun getPlayer(): Player {
            val player = Player(userID, username)
            player.position = position
            return player
        }
    }
}
package common.player

import java.util.*

class Players {
    var playersMap = Collections.synchronizedMap(HashMap<Int, Player>())
    private var nextAvailableUserID = 0

    fun addPlayer(player: Player) {
        playersMap[player.userID] = player
        println("Player ${player.username} was added!")
    }

    fun getNextUserID(): Int {
        val chosenUserID = nextAvailableUserID
        nextAvailableUserID += 1
        return chosenUserID
    }

    fun getPlayer(userID: Int): Player? {
        return playersMap[userID]
    }

    fun removePlayer(userID: Int) {
        playersMap.remove(userID)
    }
}
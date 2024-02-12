package common.player

import java.util.*

class Players {
    private var playersMap: MutableMap<Int, Player> = Collections.synchronizedMap(HashMap<Int, Player>())
    private var nextAvailableUserID = 0

    fun addPlayer(player: Player) {
        playersMap[player.userID] = player
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
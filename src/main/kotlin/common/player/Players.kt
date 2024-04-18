package common.player

import common.GameEngineProvider
import common.event.clientevents.PlayerJoinedEvent
import common.event.clientevents.PlayerLeftEvent
import server.Server
import java.util.*

class Players {
    private var playersMap: MutableMap<Int, Player> = Collections.synchronizedMap(HashMap<Int, Player>())
    private var nextAvailableUserID = 0

    fun addPlayer(player: Player) {
        val gameEngine = GameEngineProvider.getGameEngine()
        if (gameEngine.isServer()) {
            val server = gameEngine as Server
            val playerJoinedEvent = PlayerJoinedEvent(player)
            server.serverNetwork.sendEventToEveryone(playerJoinedEvent)
        }

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

    fun removePlayerByUserID(userID: Int) {
        playersMap.remove(userID)
    }

    fun removePlayer(player: Player) {
        removePlayerByUserID(player.userID)
        val gameEngine = GameEngineProvider.getGameEngine()
        if (gameEngine.isServer()) {
            val server = gameEngine as Server
            val playerLeftEvent = PlayerLeftEvent(player.userID)
            server.serverNetwork.sendEventToEveryone(playerLeftEvent)
        }
    }

    fun getPlayerList(): MutableCollection<Player> {
        return playersMap.values
    }
}
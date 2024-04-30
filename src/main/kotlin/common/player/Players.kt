package common.player

import common.GameEngineProvider
import common.event.clientevents.PlayerJoinedClientEvent
import common.event.clientevents.PlayerLeftClientEvent
import server.Server
import java.util.*

class Players {
    private var playersMap: MutableMap<Int, Player> = Collections.synchronizedMap(HashMap())
    private var nextAvailableUserID = 0
    var localPlayer: Player? = null

    fun addPlayer(player: Player) {
        val gameEngine = GameEngineProvider.getGameEngine()
        if (gameEngine.isServer()) {
            val server = gameEngine as Server
            val playerJoinedEvent = PlayerJoinedClientEvent(player)
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
            val playerLeftEvent = PlayerLeftClientEvent(player.userID)
            server.serverNetwork.sendEventToEveryone(playerLeftEvent)
        }
    }

    fun getPlayerList(): MutableCollection<Player> {
        return playersMap.values
    }
}
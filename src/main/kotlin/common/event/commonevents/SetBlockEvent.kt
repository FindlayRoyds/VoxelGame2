package common.event.commonevents

import client.Client
import common.GameEngineProvider
import common.event.Event
import common.math.Int3
import server.Server

class SetBlockEvent(position: Int3, val blockValue: Byte) : Event() {
    val x = position.x
    val y = position.y
    val z = position.z

    override fun event() {
        val gameEngine = GameEngineProvider.getGameEngine()
        if (gameEngine.isServer()) {
            println("got to server")
            val server = gameEngine as Server
            val userID = server.serverNetwork.getUserIDFromSocket(socket!!)!!
            val player = server.players.getPlayer(userID)!!
            val setBlockEvent = SetBlockEvent(Int3(x, y, z), blockValue)
            server.serverNetwork.sendEventToEveryoneExcluding(setBlockEvent, player)
        } else {
            val client = gameEngine as Client
            client.world.chunkManager.setBlock(Int3(x, y, z), blockValue)
        }
    }
}
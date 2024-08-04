package common.event.commonevents

import common.GameEngineProvider
import common.event.ServerEvent
import common.math.Int3
import server.Server

class SetBlockServerEvent(position: Int3, val blockValue: Char) : ServerEvent() {
    val x = position.x
    val y = position.y
    val z = position.z

    override fun event() {
        val server = GameEngineProvider.getGameEngine() as Server
        val userID = server.serverNetwork.getUserIDFromSocket(socket!!)!!
        val player = server.players.getPlayer(userID)!!

        val setBlockEvent = SetBlockClientEvent(Int3(x, y, z), blockValue)
        server.serverNetwork.sendEventToEveryoneExcluding(setBlockEvent, player)
    }
}
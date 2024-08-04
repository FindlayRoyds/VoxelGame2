package common.event.commonevents

import client.Client
import common.GameEngineProvider
import common.event.ClientEvent
import common.math.Int3

class SetBlockClientEvent(position: Int3, val blockValue: Char) : ClientEvent() {
    val x = position.x
    val y = position.y
    val z = position.z

    override fun event() {
        val client = GameEngineProvider.getGameEngine() as Client
        client.world.chunkManager.setBlock(Int3(x, y, z), blockValue)
    }
}
package common.event.commonevents

import client.Client
import common.GameEngineProvider
import common.block.Block
import common.event.ClientEvent
import common.math.Int3

class SetBlockClientEvent(val position: Int3, val block: Block) : ClientEvent() {
    override fun event() {
        val client = GameEngineProvider.getGameEngine() as Client
        client.world.chunkManager.setBlock(position, block)
    }
}
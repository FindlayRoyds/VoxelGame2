package common.event.clientevents

import client.Client
import common.GameEngineProvider
import common.block.blocks.Block
import common.event.ClientEvent
import common.math.Int3

class SetBlockClientEvent(val position: Int3, val block: Block) : ClientEvent() {
    override fun event() {
        val client = GameEngineProvider.gameEngine as Client
        client.world.chunkManager.setBlock(position, block)
    }
}
package common.event.servernetworkevents

import common.GameEngineProvider
import common.block.Block
import common.event.ServerEvent
import common.math.Int3
import server.Server

class SetBlockServerEvent(val position: Int3, val block: Block) : ServerEvent() {
    override fun event() {
        val server = GameEngineProvider.getGameEngine() as Server
        server.world.chunkManager.setBlock(position, block)
    }
}
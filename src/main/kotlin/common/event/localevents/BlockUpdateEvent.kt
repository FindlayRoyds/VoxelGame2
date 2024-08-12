package common.event.localevents

import common.GameEngineProvider
import common.event.Event
import common.math.Int3

class BlockUpdateEvent(val worldPosition: Int3): Event() {
    override fun event() {
        val block = GameEngineProvider.getGameEngine().world.chunkManager.getBlock(worldPosition)
        block?.update(worldPosition)
    }
}
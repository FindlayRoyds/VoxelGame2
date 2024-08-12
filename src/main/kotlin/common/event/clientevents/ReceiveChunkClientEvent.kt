package common.event.clientevents

import common.GameEngineProvider
import common.event.ClientEvent
import common.world.Chunk

class ReceiveChunkClientEvent(val transferObject: Chunk.ChunkTransferObject) : ClientEvent() {
    override fun event() {
        val newChunk = transferObject.getChunk()
        GameEngineProvider.getGameEngine().world.chunkManager.setChunk(newChunk)
    }
}
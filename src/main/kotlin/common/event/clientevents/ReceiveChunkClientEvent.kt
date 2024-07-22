package common.event.clientevents

import common.GameEngineProvider
import common.event.ClientEvent
import common.networking.transferobjects.chunkTransferObject

class ReceiveChunkClientEvent(val transferObject: chunkTransferObject) : ClientEvent() {
    override fun event() {
        val newChunk = transferObject.getChunk()
        GameEngineProvider.getGameEngine().world.chunkManager.setChunk(newChunk)
    }
}
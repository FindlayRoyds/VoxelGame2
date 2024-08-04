package common.networking.transferobjects

import common.world.Chunk
import java.io.Serializable

class chunkTransferObject(chunk: Chunk) : Serializable {
    val blockData = chunk.blockData
    val position = chunk.chunkPosition

    fun getChunk(): Chunk {
        val newChunk = Chunk(position)
        if (blockData != null)
            newChunk.loadFromBlockData(blockData)
        return newChunk
    }
}
package common.world

import common.Config
import common.math.Float3
import common.math.Int3

class ChunkManager {
    private val chunks = HashMap<Int3, Chunk>()

    fun generateChunk(position: Int3) {
        val newChunk = Chunk(position)
        chunks[newChunk.chunkPosition] = newChunk
    }

    fun getLoadedChunks(): MutableCollection<Chunk> {
        return chunks.values
    }

    fun worldPositionToChunkPosition(position: Float3): Int3 {
        return (position / Config.chunkSize).toInt3()
    }
}
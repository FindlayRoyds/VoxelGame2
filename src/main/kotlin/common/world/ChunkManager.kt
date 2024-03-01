package common.world

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
}
package common.world

import common.Config
import common.math.Double3
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

    fun worldPositionToChunkPosition(position: Double3): Int3 {
        return (position / Config.chunkSize).toInt3()
    }

    fun worldPositionToChunkPosition(position: Int3): Int3 {
        return position / Config.chunkSize
    }

    fun getChunk(position: Int3): Chunk? {
        return chunks[position]
    }

    fun isChunkLoaded(position: Int3): Boolean {
        return chunks.containsKey(position)
    }
}
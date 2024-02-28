package common.world

import org.joml.Vector3i

class ChunkManager {
    private val chunks = HashMap<Vector3i, Chunk>()

    fun generateChunk(position: Vector3i) {
        val newChunk = Chunk(position)
        chunks[newChunk.chunkPosition] = newChunk
    }

    fun getLoadedChunks(): MutableCollection<Chunk> {
        return chunks.values
    }
}
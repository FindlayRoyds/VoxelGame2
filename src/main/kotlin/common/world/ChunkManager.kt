package common.world

import common.Config
import common.math.Double3
import common.math.Int3
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue

class ChunkManager {
    private val chunks = ConcurrentHashMap<Int3, Chunk>()
    private val chunksToUploadToGPU = ConcurrentLinkedQueue<Chunk>()
    val chunkGenerationExecutor = ChunkGenerationExecutor(10)

    fun generateChunk(position: Int3) {
        /** Loads the chunk into the generation queue, call this 👍 */
        chunkGenerationExecutor.generateChunk(position)
    }

    fun buildChunk(position: Int3) {
        /** Actually creates the chunk, do not call lol the chunk generation executor calls this */
        val newChunk = Chunk(position)
        chunks[newChunk.chunkPosition] = newChunk
        newChunk.doThing()
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

    fun sendChunksToGPU() {
        while (!chunksToUploadToGPU.isEmpty()) {
            val chunk = chunksToUploadToGPU.poll()!!
            chunk.uploadToGPU()
        }
    }

    fun setBlock(worldPosition: Int3, value: Byte) {
        val chunkPosition = worldPositionToChunkPosition(worldPosition)
        val chunk = getChunk(chunkPosition)
        if (chunk == null)
            return
        val blockPosition = chunk.worldPositionToBlockPosition(worldPosition)
        chunk.setBlock(blockPosition, value)
    }

    fun uploadChunkToGPU(chunk: Chunk) {
        chunksToUploadToGPU.add(chunk)
    }

    fun getBlock(worldPosition: Int3): Byte {
        val chunkPosition = worldPositionToChunkPosition(worldPosition)
        val chunk = getChunk(chunkPosition)
        if (chunk == null)
            return 0.toByte()
        val blockPosition = chunk.worldPositionToBlockPosition(worldPosition)
        return chunk.getBlock(blockPosition)
    }
}
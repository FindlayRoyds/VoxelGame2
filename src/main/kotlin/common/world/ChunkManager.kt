package common.world

import client.Client
import common.Config
import common.GameEngineProvider
import common.math.Double3
import common.math.Int3
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue

class ChunkManager {
    private val chunks = ConcurrentHashMap<Int3, Chunk>()
    private val chunksToUploadToGPU = ConcurrentLinkedQueue<Chunk>()
    val chunkGenerationExecutor = ChunkGenerationExecutor(5)
    var chunkMeshingExecutor: ChunkMeshingExecutor? = null

    fun createChunkMeshingExecutor() {
        chunkMeshingExecutor = ChunkMeshingExecutor(4)
    }

    fun generateChunk(position: Int3) {
        /** Loads the chunk into the generation queue, call this 👍 */
        chunkGenerationExecutor.generateChunk(position)
    }

    fun buildChunk(position: Int3) {
        /** Actually creates the chunk, do not call lol the chunk generation executor calls this */
        if (getChunk(position) != null)
            return
        val newChunk = Chunk(position)
        newChunk.generate()
        chunks[newChunk.chunkPosition] = newChunk
        // newChunk.buildNeighbouringMeshes()
    }

    fun setChunk(chunk: Chunk) {
        chunks[chunk.chunkPosition] = chunk
        chunk.buildNeighbouringMeshes()
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
            // break
        }
    }

    fun setBlock(worldPosition: Int3, value: Char) {
        val chunkPosition = worldPositionToChunkPosition(worldPosition)
        val chunk = getChunk(chunkPosition) ?: return
        val blockPosition = chunk.worldPositionToBlockPosition(worldPosition)
        chunk.setBlock(blockPosition, value)
    }

    fun uploadChunkToGPU(chunk: Chunk) {
        chunksToUploadToGPU.add(chunk)
    }

    fun getBlock(worldPosition: Int3): Char {
        val chunkPosition = worldPositionToChunkPosition(worldPosition)
        val chunk = getChunk(chunkPosition) ?: return 0.toChar()

        val blockPosition = chunk.worldPositionToBlockPosition(worldPosition)
        return chunk.getBlock(blockPosition)
    }

    fun unloadChunks() {
        val gameEngine = GameEngineProvider.getGameEngine()
        var playerList = gameEngine.players.getPlayerList()
        if (gameEngine.isClient()) {
            val client = gameEngine as Client
            client.players.localPlayer ?: return

            playerList = mutableListOf(client.players.localPlayer!!)
        }

        val chunksToKeepLoaded = HashSet<Chunk>()

        for (chunk in getLoadedChunks()) {
            for (player in playerList) {
                val distance = (player.position - chunk.getWorldPosition()).magnitude
                if (distance <= (Config.renderDistance + Config.unloadDistance + if (gameEngine.isClient()) 0 else 1) * Config.chunkSize) {
                    chunksToKeepLoaded.add(chunk)
                }
            }
        }

        for (chunk in getLoadedChunks()) {
            if (!chunksToKeepLoaded.contains(chunk)) {
                removeChunk(chunk.chunkPosition)
            }
        }
    }

    private fun removeChunk(position: Int3) {
        val chunk = getChunk(position)
        if (chunk != null) {
            chunk.cleanup()
            chunks.remove(position)
        }
    }
}
package common.world

import common.Config
import common.GameEngineProvider
import common.math.Double3
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue

class ChunkMeshingExecutor(private val numThreads: Int) {
    // A bit cursed, make a concurrent hash set
//    private val chunkSet = Collections.newSetFromMap(ConcurrentHashMap<Chunk, Boolean>())
    private val chunkQueue = LinkedBlockingQueue<Chunk>()
    private val threadPool = Executors.newFixedThreadPool(numThreads)
    private var running = false

    fun run() {
        running = true
        val gameEngine = GameEngineProvider.getGameEngine()
        for (i in 0..<numThreads) {
            threadPool.execute {
                GameEngineProvider.setGameEngine(gameEngine)
                while (running) {
//                    val client = GameEngineProvider.getGameEngine() as Client
//                    val playerPosition = client.renderer.camera.position
//
//                    var closestChunk: Chunk? = null
//                    var closestDistance = Double.POSITIVE_INFINITY
//                    for (chunk in chunkSet) {
//                        val chunkPosition = (chunk.chunkPosition * Config.chunkSize).toDouble3()
//                        val distance = (chunkPosition - playerPosition).magnitude
//                        if (distance < closestDistance) {
//                            closestChunk = chunk
//                            closestDistance = distance
//                        }
//                    }
//
//                    if (closestChunk != null) {
//                        chunkSet.remove(closestChunk)
//                        closestChunk.buildMesh()
//                    }
                    val chunk = chunkQueue.take()
                    chunk.buildMesh()
                    // println(chunkQueue.size)

//                    val client = GameEngineProvider.getGameEngine() as Client
//                    val playerPosition = client.renderer.camera.position
//
//
//                    val closestChunk = chunkSet.minByOrNull { getChunkDistance(it, playerPosition) }
//                    if (closestChunk != null) {
//                        chunkSet.remove(closestChunk)
//                        closestChunk.buildMesh()
//                    }
                }
            }
        }
    }

    fun getChunkDistance(chunk: Chunk, playerPosition: Double3): Double {
        val chunkPosition = (chunk.chunkPosition * Config.chunkSize).toDouble3()
        return (chunkPosition - playerPosition).magnitude
    }

    fun addChunk(chunk: Chunk) {
//         chunkSet.add(chunk)
         chunkQueue.add(chunk)
        // chunk.buildMesh()
    }

    fun cleanup() {
        running = false
    }
}
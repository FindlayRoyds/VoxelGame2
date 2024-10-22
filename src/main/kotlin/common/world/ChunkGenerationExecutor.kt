package common.world

import common.Config
import common.GameEngineProvider
import common.math.Int3
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue

class ChunkGenerationExecutor(private val numThreads: Int) {
    private val chunkQueue = LinkedBlockingQueue<Int3>()
    private val chunkSet = Collections.newSetFromMap(ConcurrentHashMap<Int3, Boolean>())
    private val threadPool = Executors.newFixedThreadPool(numThreads)
    private var running = false

    fun run() {
        running = true
        val gameEngine = GameEngineProvider.gameEngine
        for (i in 0..<numThreads) {
            threadPool.execute {
                GameEngineProvider.gameEngine = gameEngine
                while (running) {

                    val position = chunkQueue.take()
//                    val position = chunkSet.minByOrNull { getMinDistanceFromAllPlayer(it) }
                    chunkSet.remove(position)
                    gameEngine.world.chunkManager.buildChunk(position)
                }
            }
        }
    }

    fun getMinDistanceFromAllPlayer(chunkPosition: Int3): Double {
        val position = (chunkPosition * Config.chunkSize).toDouble3()
        val gameEngine = GameEngineProvider.gameEngine
        return gameEngine.players.getPlayerList().minOf { (position - it.position).magnitude }
    }

    fun generateChunk(position: Int3) {
        if (!chunkSet.contains(position) && chunkQueue.size < numThreads * 100) {
            chunkQueue.put(position)
            chunkSet.add(position)
        }
    }

    fun cleanup() {
        running = false
    }
}
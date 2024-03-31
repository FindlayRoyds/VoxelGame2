package common.world

import common.GameEngineProvider
import common.math.Int3
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue

class ChunkGenerationExecutor(private val numThreads: Int) {
    private val chunkQueue = LinkedBlockingQueue<Int3>()
    private val threadPool = Executors.newFixedThreadPool(numThreads)
    private var running = false

    fun run() {
        running = true
        val gameEngine = GameEngineProvider.getGameEngine()
        for (i in 0..<numThreads) {
            threadPool.execute {
                GameEngineProvider.setGameEngine(gameEngine)
                while (running) {

                    val position = chunkQueue.take()
                    gameEngine.world.chunkManager.buildChunk(position)
                }
            }
        }
    }

    fun generateChunk(position: Int3) {
        chunkQueue.put(position)
    }

    fun cleanup() {
        running = false
    }
}
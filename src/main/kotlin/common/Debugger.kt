package common

import common.math.Double3
import common.math.Int3

object Debugger {
    var previousRenderTime = System.nanoTime()
    var fps = 0
    private const val fpsFrameInfluence = 0.02

    var position = Double3(0, 0, 0)
    var chunk = Int3(0, 0, 0)

    var totalChunkMeshingTimeNano = 0L
    var chunkMeshingCount = 0L
    var averageChunkMeshingTimeNano = 0.0

    var totalChunkGenerationTimeNano = 0L
    var chunkGenerationCount = 0L
    var averageChunkGenerationTimeNano = 0.0

    var ipAddress = "Unknown"

    var menuVisible = false

    fun updateFps() {
        val currentTime = System.nanoTime()
        val currentFps = 1_000_000_000 / (currentTime - previousRenderTime)
        fps = (fps * (1 - fpsFrameInfluence) + currentFps * fpsFrameInfluence).toInt()
        previousRenderTime = currentTime
    }

    fun logChunkMeshingTime(timeNano: Long) {
        totalChunkMeshingTimeNano += timeNano
        chunkMeshingCount += 1

        averageChunkMeshingTimeNano = totalChunkMeshingTimeNano / chunkMeshingCount.toDouble()
    }

    fun logChunkGenerationTime(timeNano: Long) {
        totalChunkGenerationTimeNano += timeNano
        chunkGenerationCount += 1

        averageChunkGenerationTimeNano = totalChunkGenerationTimeNano / chunkGenerationCount.toDouble()
    }
}
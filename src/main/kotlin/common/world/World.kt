package common.world

import common.block.blocks.Air
import common.math.Double3
import common.math.Int3
import kotlin.math.max
import kotlin.random.Random

class World {
    val chunkManager = ChunkManager()
    val seed = Random.nextInt()

    fun getVoxel(worldPosition: Int3): Boolean {
        val block = chunkManager.getBlock(worldPosition)
        return block !is Air
    }

    fun raycast(origin: Double3, direction: Double3, maxDistance: Double): Pair<Int3, Int3>? {
        val stepsPerVoxel = 20
        val steps = (maxDistance * stepsPerVoxel).toInt()

        for (step in 0..steps) {
            val worldPosition = (origin + direction * step / stepsPerVoxel).toInt3()
            if (getVoxel(worldPosition)) {
                val previousBlockStep = max(0, step - 1)
                val previousBlock = (origin + direction * previousBlockStep / stepsPerVoxel).toInt3()
                return Pair(worldPosition, previousBlock)
            }
        }

        return null
    }
}
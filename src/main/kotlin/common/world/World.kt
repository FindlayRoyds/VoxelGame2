package common.world

import common.block.blocks.Air
import common.math.Double3
import common.math.Int3
import kotlin.math.max
import kotlin.random.Random

class World {
    val chunkManager = ChunkManager()
    val seed = Random.nextInt()
    var tick = 0 // TODO synchronise between client and server when initialised? idk if needed

    /*
    Returns the hit block and the previous block before the hit block if that makes sense
    ray is not normalised, it includes the max distance
     */
    fun raycast(origin: Double3, ray: Double3, includeNotSolidBlocks: Boolean): Pair<Int3, Int3>? {
        val stepsPerVoxel = 20
        val steps = (ray.magnitude * stepsPerVoxel).toInt()
        val direction = ray.normal

        for (step in 0..steps) {
            val worldPosition = (origin + direction * step / stepsPerVoxel).floor()
            val block = chunkManager.getBlock(worldPosition) ?: return null
            if (block.isSolid || includeNotSolidBlocks && block !is Air) {
                val previousBlockStep = max(0, step - 1)
                val previousBlock = (origin + direction * previousBlockStep / stepsPerVoxel).floor()
                return Pair(worldPosition, previousBlock)
            }
        }

        return null
    }
}
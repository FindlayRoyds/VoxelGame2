package common.world

import client.graphics.Mesh
import org.joml.Vector3i

class Chunk(val chunkPosition: Vector3i) {
    var mesh: Mesh? = null
    var blockData = BooleanArray(32 * 32 * 32)

    init {
        generate()
        buildMesh()
    }

    fun buildMesh() {
        val blockVertexIds = intArrayOf(
            1, 0, 2, 4, 3, 5,
            6, 7, 8, 9, 10, 11,
            12, 13, 14, 15, 16, 17,
            19, 18, 20, 22, 21, 23,
            24, 25, 26, 27, 28, 29,
            31, 30, 32, 34, 33, 35
        )

        val vertexIds = ArrayList<Int>()
        val blockPositions = ArrayList<Int>()

        for (blockIndex in blockData.indices) {
            val randomInt = (0..7).random()
            for (vertexId in blockVertexIds) {
                if (blockData[blockIndex]) {
                    vertexIds.add(vertexId)
                    blockPositions.add(blockIndex)
                }
            }
        }

        mesh = Mesh(vertexIds.toIntArray(), blockPositions.toIntArray())
    }

    fun generate() {
        for (blockIndex in blockData.indices) {
            val blockPosition = blockIndexToBlockPosition(blockIndex)
            val blockWorldPosition = blockPositionToWorldPosition(blockPosition)
            if ((blockWorldPosition.y + blockWorldPosition.x + blockWorldPosition.z) % 16 == 0) {
                blockData[blockIndex] = true
            } else {
                blockData[blockIndex] = false
            }
        }
    }

    private fun blockPositionToBlockIndex(blockPosition: Vector3i): Int {
        return (blockPosition.x *  32 *  32) + (blockPosition.y *  32) + blockPosition.z
    }

    private fun blockIndexToBlockPosition(blockIndex: Int): Vector3i {
        return Vector3i(blockIndex / (32 * 32), (blockIndex / 32) % 32, blockIndex % 32)
    }

    private fun blockPositionToWorldPosition(blockPosition: Vector3i): Vector3i {
        return Vector3i(chunkPosition).mul(32).add(blockPosition)
    }
}
package common.world

import client.graphics.Mesh
import common.Config
import common.math.Int3
import kotlin.math.sin


class Chunk(val chunkPosition: Int3) {
    var mesh: Mesh? = null
    var blockData = BooleanArray(Config.chunkSize * Config.chunkSize * Config.chunkSize)
    val blockNeighbors = ArrayList<Int3>(26)

    init {
        calculateBlockNeighborValues()

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
            if (!isBlockVisible(blockIndexToBlockPosition(blockIndex)))
                continue

            for (vertexId in blockVertexIds) {
                if (blockData[blockIndex]) {
                    vertexIds.add(vertexId)
                    blockPositions.add(blockIndex)
                }
            }
        }

        mesh = Mesh(vertexIds.toIntArray(), blockPositions.toIntArray())
    }

    private fun isBlockVisible(position: Int3): Boolean {
        for (neighbor in blockNeighbors) {

        }
        return true
    }

    fun generate() {
        for (blockIndex in blockData.indices) {
            val blockPosition = blockIndexToBlockPosition(blockIndex)
            val worldPosition = blockPositionToWorldPosition(blockPosition)
            // if ((blockWorldPosition.y + blockWorldPosition.x + blockWorldPosition.z) % 32 == 0) {
            // if (blockWorldPosition.y + (blockPosition.x % 2) + ((blockPosition.z / 4) % 2) <= 2) {
            if ((
                sin(worldPosition.x.toDouble() / 47 - worldPosition.z.toDouble() / 53) * 37
                + sin(worldPosition.x.toDouble() / 5 + worldPosition.z.toDouble() / 3) * 2
                + sin(worldPosition.x.toDouble() / 10) * 6
                + sin(worldPosition.z.toDouble() / 7) * 3
                ).toInt() + worldPosition.y - 18 in -1..0) {
                blockData[blockIndex] = true
            } else {
                blockData[blockIndex] = false
            }
        }
    }

    private fun blockPositionToBlockIndex(blockPosition: Int3): Int {
        return (blockPosition.x *  Config.chunkSize *  Config.chunkSize) + (blockPosition.y *  Config.chunkSize) + blockPosition.z
    }

    private fun blockIndexToBlockPosition(blockIndex: Int): Int3 {
        return Int3(blockIndex / (Config.chunkSize * Config.chunkSize), (blockIndex / Config.chunkSize) % Config.chunkSize, blockIndex % Config.chunkSize)
    }

    private fun blockPositionToWorldPosition(blockPosition: Int3): Int3 {
        return chunkPosition * Config.chunkSize + blockPosition
    }

    private fun isBlockPositionInChunk(blockPosition: Int3): Boolean {
        if (blockPosition.x !in 0 until Config.chunkSize)
            return false
        if (blockPosition.y !in 0 until Config.chunkSize)
            return false
        if (blockPosition.z !in 0 until Config.chunkSize)
            return false
        return true
    }

    private fun calculateBlockNeighborValues() {
        for (x in -1..1) {
            for (y in -1..1) {
                for (z in -1..1) {
                    val neighbor = Int3(x, y, z)
                    if (neighbor != Int3(0, 0, 0)) {
                        blockNeighbors.add(neighbor)
                    }
                }
            }
        }
    }
}
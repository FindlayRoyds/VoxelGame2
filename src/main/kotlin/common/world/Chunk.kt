package common.world

import client.Client
import client.graphics.Mesh
import common.Config
import common.GameEngineProvider
import common.math.Int3
import kotlin.math.sin


class Chunk(val chunkPosition: Int3) {
    var mesh: Mesh? = null
    var blockData = BooleanArray(Config.chunkSize * Config.chunkSize * Config.chunkSize)
    var heightmapCache = HashMap<Int3, Int>()
    var blockNeighbors = ArrayList<Int3>(26)

    init {
        calculateBlockNeighborValues()

        generate()
    }

    fun getBlock(blockPosition: Int3): Boolean {
        if (!isBlockPositionInChunk(blockPosition))
            return false
        val blockIndex = blockPositionToBlockIndex(blockPosition)
        return blockData[blockIndex]
    }

    fun buildMesh() {
        val blockVertexIds = intArrayOf(
            1, 0, 2, 4, 3, 5,
            6, 7, 8, 9, 10, 11,
            12, 13, 14, 15, 16, 17,
            19, 18, 20, 22, 21, 23,
            24, 25, 26, 27, 28, 29, // top
            31, 30, 32, 34, 33, 35 // bottom
        )

        val vertexIds = ArrayList<Int>()
        val blockPositions = ArrayList<Int>()

        for (blockIndex in blockData.indices) {
            if (!blockData[blockIndex])
                continue
            val blockPosition = blockIndexToBlockPos(blockIndex)

            for (faceIndex in 0..5) {
                if (isFaceVisible(blockPosition, faceIndex)) {
                    for (vertexIndex in faceIndex * 6..(faceIndex * 6+5)) {
                        vertexIds.add(blockVertexIds[vertexIndex])
                        blockPositions.add(blockIndex)
                    }
                }
            }
        }

        mesh = Mesh(vertexIds.toIntArray(), blockPositions.toIntArray())
    }

    private fun isFaceVisible(blockPosition: Int3, faceIndex: Int): Boolean {
        val neighbor = blockNeighbors[faceIndex]
        val neighborPosition = blockPosition + neighbor
        if (!isBlockPositionInChunk(neighborPosition)) {

            val chunkManager = (GameEngineProvider.getGameEngine() as Client).world.chunkManager
            val neighborWorldPosition = blockPositionToWorldPosition(neighborPosition)
            val neighborChunkPosition = chunkManager.worldPositionToChunkPosition(neighborWorldPosition)
            if (!chunkManager.isChunkLoaded(neighborChunkPosition))
                return false
            val neighborChunk = chunkManager.getChunk(neighborChunkPosition)!!
            val neighborChunkBlockPosition = neighborChunk.worldPositionToBlockPosition(neighborWorldPosition)
            return !neighborChunk.getBlock(neighborChunkBlockPosition)
        } else {
            return !getBlock(neighborPosition)
        }
    }

    fun generate() {
        for (blockIndex in blockData.indices) {
            val blockPosition = blockIndexToBlockPos(blockIndex)
            val worldPosition = blockPositionToWorldPosition(blockPosition)
            var height: Int
            if (heightmapCache.contains(blockPosition * Int3(1, 0, 1))) {
                height = heightmapCache.get(blockPosition * Int3(1, 0, 1))!!
            } else {
                height = (sin(worldPosition.x.toDouble() / 47 - worldPosition.z.toDouble() / 53) * 37
                        + sin(worldPosition.x.toDouble() / 5 + worldPosition.z.toDouble() / 3) * 2
                        + sin(worldPosition.x.toDouble() / 10) * 6
                        + sin(worldPosition.z.toDouble() / 7) * 3
                        ).toInt()
                heightmapCache[blockPosition * Int3(1, 0, 1)] = height
            }
            if (height + worldPosition.y - 18 < 0) {
                blockData[blockIndex] = true
            } else {
                blockData[blockIndex] = false
            }
        }
    }

    fun blockPositionToBlockIndex(blockPosition: Int3): Int {
        return (blockPosition.x *  Config.chunkSize *  Config.chunkSize) + (blockPosition.y *  Config.chunkSize) + blockPosition.z
    }

    fun blockIndexToBlockPos(blockIndex: Int): Int3 {
        return Int3(blockIndex / (Config.chunkSize * Config.chunkSize), (blockIndex / Config.chunkSize) % Config.chunkSize, blockIndex % Config.chunkSize)
    }

    fun blockPositionToWorldPosition(blockPosition: Int3): Int3 {
        return chunkPosition * Config.chunkSize + blockPosition
    }

    fun worldPositionToBlockPosition(worldPosition: Int3): Int3 {
        return worldPosition - chunkPosition * Config.chunkSize
    }

    fun isBlockPositionInChunk(blockPosition: Int3): Boolean {
        if (blockPosition.x !in 0 until Config.chunkSize)
            return false
        if (blockPosition.y !in 0 until Config.chunkSize)
            return false
        if (blockPosition.z !in 0 until Config.chunkSize)
            return false
        return true
    }

    fun calculateBlockNeighborValues() {
        blockNeighbors = arrayListOf(
            Int3(0, 0, -1), Int3(0, 0, 1),
            Int3(-1, 0, 0), Int3(1, 0, 0),
            Int3(0, -1, 0), Int3(0, 1, 0),
        )
    }
}
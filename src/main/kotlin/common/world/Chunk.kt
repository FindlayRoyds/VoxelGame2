package common.world

import client.Client
import client.graphics.Mesh
import client.graphics.MeshData
import common.Config
import common.GameEngineProvider
import common.math.Int3
import common.world.noise.FastNoiseLite
import kotlin.math.floor


class Chunk(val chunkPosition: Int3) {
    var mesh: Mesh? = null
    var meshData: MeshData? = null
    var blockData = ByteArray(Config.chunkSize * Config.chunkSize * Config.chunkSize)
    var heightmapCache = HashMap<Int, Int>()
    val gameEngine = GameEngineProvider.getGameEngine() as Client
    var blockNeighbors = arrayListOf(Int3(0, 0, -1), Int3(0, 0, 1), Int3(-1, 0, 0), Int3(1, 0, 0), Int3(0, -1, 0), Int3(0, 1, 0))

    init {
        generate()
    }

    fun getBlock(blockPosition: Int3): Byte {
        if (!isBlockPositionInChunk(blockPosition))
            return 0
        val blockIndex = blockPositionToBlockIndex(blockPosition)
        return blockData[blockIndex]
    }

    fun buildMesh() {
        for (neighborOffset in blockNeighbors) {
            val neighborPosition = chunkPosition + neighborOffset
            if (gameEngine.world.chunkManager.getChunk(neighborPosition) == null)
                return
        }
        
        meshData = null

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
        val blockTypes = ArrayList<Int>()

        val packedValues = ArrayList<Int>()

        for (blockIndex in blockData.indices) {
            if (blockData[blockIndex] == 0.toByte())
                continue
            val blockPosition = blockIndexToBlockPos(blockIndex)
//            if (!isBlockVisible(blockPosition))
//                continue

            for (faceIndex in 0..5) {
                 if (isFaceVisible(blockPosition, faceIndex)) {
                    for (vertexIndex in faceIndex * 6..(faceIndex * 6+5)) {
//                        vertexIds.add(blockVertexIds[vertexIndex])
//                        blockPositions.add(blockIndex)
//                        blockTypes.add(blockData[blockIndex].toInt() - 1)

                        val blockType = blockData[blockIndex].toInt() - 1
                        val blockVertexID = blockVertexIds[vertexIndex]

                        val packedData = (blockIndex shl 16) or (blockType shl 8) or blockVertexID
                        packedValues.add(packedData)
                    }
                 }
            }
        }

//        meshData = MeshData(vertexIds.toIntArray(), blockPositions.toIntArray(), blockTypes.toIntArray())
        meshData = MeshData(packedValues.toIntArray())
        gameEngine.world.chunkManager.uploadChunkToGPU(this)
    }

    fun uploadToGPU() {
        if (meshData != null)
            mesh = Mesh(meshData!!)
    }

    private fun isFaceVisible(blockPosition: Int3, faceIndex: Int): Boolean {
        val neighbor = blockNeighbors[faceIndex]
        val neighborPosition = blockPosition + neighbor
        if (!isBlockPositionInChunk(neighborPosition)) {

            val chunkManager = gameEngine.world.chunkManager
            val neighborWorldPosition = blockPositionToWorldPosition(neighborPosition)
            val neighborChunkPosition = chunkManager.worldPositionToChunkPosition(neighborWorldPosition)
            if (!chunkManager.isChunkLoaded(neighborChunkPosition))
                return false
            val neighborChunk = chunkManager.getChunk(neighborChunkPosition)!!
            val neighborChunkBlockPosition = neighborChunk.worldPositionToBlockPosition(neighborWorldPosition)
            return neighborChunk.getBlock(neighborChunkBlockPosition) == 0.toByte()
        } else {
            return getBlock(neighborPosition) == 0.toByte()
        }
    }

    private fun isBlockVisible(blockPosition: Int3): Boolean {
        for (faceIndex in 0..5) {
            if (isFaceVisible(blockPosition, faceIndex)) {
                return true
            }
        }
        return false
    }

    fun generate() {
        val noise = FastNoiseLite()
        noise.SetSeed(1)

        for (blockIndex in blockData.indices) {
            val blockPosition = blockIndexToBlockPos(blockIndex)
            val worldPosition = blockPositionToWorldPosition(blockPosition)
            var height: Int
            if (heightmapCache.contains(blockPosition.x * 33 + blockPosition.z)) {
                height = heightmapCache.get(blockPosition.x * 33 + blockPosition.z)!!
            } else {
////                height = floor(
////                    (sin(worldPosition.x.toDouble() / 53.0) - sin(worldPosition.z.toDouble() / -59.0)) * 53
////                            + sin(worldPosition.x.toDouble() / 27.0 - worldPosition.z.toDouble() / 33.0) * 19
////                            + sin(worldPosition.x.toDouble() / 5.0 + worldPosition.z.toDouble() / 3.0) * 2
////                            + sin(worldPosition.x.toDouble() / 10.0) * 6
////                            + sin(worldPosition.z.toDouble() / 7.0) * 3
////                ).toInt()
//
                val x = worldPosition.x.toFloat()
                val y = worldPosition.z.toFloat()
                noise.SetNoiseType(FastNoiseLite.NoiseType.Cellular)
                val cellularNoiseResult = noise.GetNoise(x, y)
                noise.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2)
                val simplexNoiseResult = noise.GetNoise(x, y)
                height = floor(Math.min(cellularNoiseResult * -70 + simplexNoiseResult * 10, simplexNoiseResult * 7 + 50)).toInt()
                heightmapCache[blockPosition.x * 33 + blockPosition.z] = height
            }
            if (height + worldPosition.y - 18 < -4) {
                blockData[blockIndex] = 2.toByte()
            } else if (height + worldPosition.y - 18 < 0) {
                blockData[blockIndex] = 3.toByte()
            } else if (height + worldPosition.y - 18 == 0) {
                blockData[blockIndex] = 1.toByte()
            } else {
                blockData[blockIndex] = 0.toByte()
            }
            noise.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2S)
            val simplexNoiseResult = noise.GetNoise(worldPosition.x.toFloat() * 3, worldPosition.y.toFloat() * 2, worldPosition.z.toFloat() * 3)
            noise.SetSeed(2)
            val simplexNoiseResult2 = noise.GetNoise(worldPosition.x.toFloat() * 3, worldPosition.y.toFloat() * 3, worldPosition.z.toFloat() * 3)
            noise.SetSeed(1)
            if (simplexNoiseResult in -0.2.. 0.2 && simplexNoiseResult2 > 0.5) {
                blockData[blockIndex] = 0.toByte()
            }
        }

//        for (i in 0..1) {
//            val treePosition = Int2(Random.nextInt(0, Config.chunkSize), Random.nextInt(0, Config.chunkSize))
//            val height = heightmapCache[treePosition.x * 33 + treePosition.y]
//            if (height != null) {
//                val blockPosition = Int3(treePosition.x + chunkPosition.x * Config.chunkSize, -height + 19, treePosition.y + chunkPosition.z * Config.chunkSize)
//                // if (isBlockPositionInChunk(blockPosition)) {
////                    val blockIndex = blockPositionToBlockIndex(blockPosition)
////                    blockData[blockIndex] = 2.toByte()
//                for (y in 0..4)
//                    GameEngineProvider.getGameEngine().world.chunkManager.setBlock(blockPosition + Int3(0, y, 0), 2)
//                // }
//            } else {
//                println("null on $treePosition")
//            }
//        }
        // println(heightmapCache[0])
    }

    fun doThing() {
        // buildMesh()
        for (neighborOffset in blockNeighbors) {
            val neighborPosition = chunkPosition + neighborOffset
            gameEngine.world.chunkManager.getChunk(neighborPosition)?.buildMesh()
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

    fun setBlock(blockPosition: Int3, value: Byte) {
        val blockIndex = blockPositionToBlockIndex(blockPosition)
        if (blockData[blockIndex] == value)
            return
        blockData[blockIndex] = value
        buildMesh()

        for (neighborDirection in chunksNeighboringBlock(blockPosition)) {
            val neighborPosition = chunkPosition + neighborDirection
            val neighborChunk = gameEngine.world.chunkManager.getChunk(neighborPosition)
            neighborChunk?.buildMesh()
        }
    }

    fun chunksNeighboringBlock(blockPosition: Int3): List<Int3> {
        val result = mutableListOf<Int3>()

        if (blockPosition.x == 0)
            result.add(Int3(-1, 0, 0))
        else if (blockPosition.x == 31)
            result.add(Int3(1, 0, 0))
        if (blockPosition.y == 0)
            result.add(Int3(0, -1, 0))
        else if (blockPosition.y == 31)
            result.add(Int3(0, 1, 0))
        if (blockPosition.z == 0)
            result.add(Int3(0, 0, -1))
        else if (blockPosition.z == 31)
            result.add(Int3(0, 0, 1))

        return result
    }
}
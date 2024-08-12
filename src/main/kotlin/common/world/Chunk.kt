package common.world

import client.graphics.Mesh
import client.graphics.MeshData
import common.Config
import common.GameEngineProvider
import common.block.Block
import common.block.blocks.Air
import common.block.blocks.Dirt
import common.block.blocks.Stone
import common.math.Double3
import common.math.Int3
import common.utils.Utils
import common.world.noise.FastNoiseLite
import java.io.Serializable
import kotlin.math.floor

class Chunk(val chunkPosition: Int3) {
    var mesh: Mesh? = null
    var meshData: MeshData? = null
    val gameEngine = GameEngineProvider.getGameEngine()
    val chunkManager = gameEngine.world.chunkManager
    private var blockPalette: BlockPalette

    var timesBuilt = 0

    var creationTime: Long = 0
    var firstMesh = true
    var changed = false

    init {
        blockPalette = BlockPalette()
    }

    constructor(chunkPosition: Int3, blockPalette: BlockPalette) : this(chunkPosition) {
        this.blockPalette = blockPalette
    }

    fun getBlock(blockPosition: Int3): Block {
        require(isBlockPositionInChunk(blockPosition)) { "Block position $blockPosition not in chunk $chunkPosition!" }
        return blockPalette.get(blockPosition)//.copy()
    }

    fun setBlock(blockPosition: Int3, block: Block) {
        if (blockPalette.get(blockPosition) == block)
            return
        blockPalette.set(blockPosition, block)

        timesBuilt = 0
        changed = true

        for (neighborDirection in chunksNeighboringBlock(blockPosition)) {
            val neighborPosition = chunkPosition + neighborDirection
            val neighborChunk = gameEngine.world.chunkManager.getChunk(neighborPosition)
            neighborChunk?.timesBuilt = 0
            neighborChunk?.changed = true
        }
    }

    fun buildMesh() {
        if (GameEngineProvider.getGameEngine().isServer())
            return
        if (timesBuilt > 0) {
            this.timesBuilt++
            return
        }
        for (neighborOffset in Utils.blockNeighbors) {
            val neighborPosition = chunkPosition + neighborOffset
            if (!gameEngine.world.chunkManager.isChunkLoaded(neighborPosition))
                return
        }
        this.timesBuilt++
        if (firstMesh) {
            creationTime = System.currentTimeMillis()
            firstMesh = false
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

        val packedValues = ArrayList<Int>()

        if (blockPalette.singleBlockType == null) {
            for ((block, blockPosition) in blockPalette.blocksWithPositions) {
                if (block is Air)
                    continue

                for (faceIndex in 0..5) {
                    if (isFaceVisible(blockPosition, faceIndex)) {
                        for (vertexIndex in faceIndex * 6..(faceIndex * 6 + 5)) {
                            val blockType = getBlock(blockPosition).id.toInt() - 1
                            val blockVertexID = blockVertexIds[vertexIndex]

                            val blockIndex = blockPositionToBlockIndex(blockPosition)
                            val packedData = (blockIndex shl 16) or (blockType shl 8) or blockVertexID
                            packedValues.add(packedData)
                        }
                    }
                }
            }
        }

        meshData = MeshData(packedValues.toIntArray())
        gameEngine.world.chunkManager.uploadChunkToGPU(this)
    }

    fun uploadToGPU() {
        if (meshData != null) {
            mesh = Mesh(meshData!!)
        }
    }

    private fun isFaceVisible(blockPosition: Int3, faceIndex: Int): Boolean {
        val neighbor = Utils.blockNeighbors[faceIndex]
        val neighborPosition = blockPosition + neighbor
        if (!isBlockPositionInChunk(neighborPosition)) {

            val chunkManager = gameEngine.world.chunkManager
            val neighborWorldPosition = blockPositionToWorldPosition(neighborPosition)
            val neighborChunkPosition = chunkManager.worldPositionToChunkPosition(neighborWorldPosition)
            if (!chunkManager.isChunkLoaded(neighborChunkPosition))
                return false
            val neighborChunk = chunkManager.getChunk(neighborChunkPosition)!!
            val neighborChunkBlockPosition = neighborChunk.worldPositionToBlockPosition(neighborWorldPosition)
            return neighborChunk.getBlock(neighborChunkBlockPosition) is Air
        } else {
            return getBlock(neighborPosition) is Air
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
        noise.SetSeed(gameEngine.world.seed)

        blockPalette = BlockPalette()

        for (blockPosition in blockPalette.positions) {
            val worldPosition = blockPositionToWorldPosition(blockPosition)
            var height = chunkManager.getHeight(worldPosition.xz)
            if (height == null) {
                val x = worldPosition.x.toFloat()
                val y = worldPosition.z.toFloat()
                noise.SetNoiseType(FastNoiseLite.NoiseType.Cellular)
                val cellularNoiseResult = noise.GetNoise(x * 2, y * 2)
                noise.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2)
                val simplexNoiseResult1 = noise.GetNoise(x, y)
                noise.SetSeed(gameEngine.world.seed + 2)
                val simplexNoiseResult2 = noise.GetNoise(x / 4, y / 4)
                height = floor(Math.min(cellularNoiseResult * -50 * (-0.5f * simplexNoiseResult2 + 0.5f) + 20 + simplexNoiseResult1 * 10, simplexNoiseResult1 * 7 + 50) + simplexNoiseResult2 * 40).toInt()
                var heightmapChunk = chunkManager.getHeightmapChunk(chunkPosition.xz)
                if (heightmapChunk == null)
                    heightmapChunk = HeightmapChunk(chunkPosition.xz)
                heightmapChunk.setHeight(worldPosition.xz, height)
            }
            if (height + worldPosition.y - 18 < -4) {
                blockPalette.set(blockPosition, Stone())
            } else if (height + worldPosition.y - 18 < 0) {
                blockPalette.set(blockPosition, Stone())
            } else if (height + worldPosition.y - 18 == 0) {
                blockPalette.set(blockPosition, Dirt())
            } else {
                blockPalette.set(blockPosition, Air())
            }
            noise.SetSeed(gameEngine.world.seed)
//            noise.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2S)
//            val simplexNoiseResult = noise.GetNoise(worldPosition.x.toFloat() * 3, worldPosition.y.toFloat() * 2, worldPosition.z.toFloat() * 3)
//            noise.SetSeed(gameEngine.world.seed + 1)
//            val simplexNoiseResult2 = noise.GetNoise(worldPosition.x.toFloat() * 3, worldPosition.y.toFloat() * 3, worldPosition.z.toFloat() * 3)
//            noise.SetSeed(gameEngine.world.seed)
//            if (simplexNoiseResult in -0.2.. 0.2 && simplexNoiseResult2 > 0.5) {
//                 blockData[blockIndex] = 0.toChar()
//            }
        }

        blockPalette.updateSingleBlockType()
    }

    fun loadFromBlockPalette(newPalette: BlockPalette) {
        blockPalette = newPalette
        blockPalette.updateSingleBlockType()
    }

    fun buildNeighboringMeshes() {
        for (neighborOffset in Utils.blockNeighbors) {
            val neighborPosition = chunkPosition + neighborOffset
            val neighbor = gameEngine.world.chunkManager.getChunk(neighborPosition)
            if (neighbor != null) {
                gameEngine.world.chunkManager.chunkMeshingExecutor!!.addChunk(neighbor)
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

    fun getWorldPosition(): Double3 {
        return (chunkPosition.toDouble3() + Double3(0.5, 0.5, 0.5)) * Config.chunkSize
    }

    fun cleanup() {
        mesh?.cleanup()
    }

    fun getTransferObject(): ChunkTransferObject {
        return ChunkTransferObject(this)
    }

    class ChunkTransferObject(chunk: Chunk): Serializable {
        private val blockPalette = chunk.blockPalette
        private val position = chunk.chunkPosition

        fun getChunk(): Chunk {
            return Chunk(position, blockPalette)
        }
    }
}
package common.world

import common.Config
import common.block.Block
import common.block.blocks.Air
import common.math.Int3
import common.utils.PackedArray
import java.io.Serializable

infix fun Int.pow(exponent: Int): Int {
    var result = 1
    repeat(exponent) {
        result *= this
    }
    return result
}

class BlockPalette: Serializable {
    private val arraySize = Config.chunkSize * Config.chunkSize * Config.chunkSize
    private val palette = ArrayList<Block>()
    private var paletteIndex: PackedArray? = null
    var singleBlockType: Block? = Air()

    val blocks: List<Block>
        get() = (0..<arraySize).map { get(it) }
    val positions: List<Int3>
        get() = (0..<arraySize).map { blockIndexToBlockPos(it) }
    val blocksWithPositions: List<Pair<Block, Int3>>
        get() = blocks.zip(positions)

    fun get(blockPosition: Int3): Block {
        val blockIndex = blockPositionToBlockIndex(blockPosition)
        return get(blockIndex)
    }

    fun get(blockIndex: Int): Block {
        if (singleBlockType != null)
            return singleBlockType!!
        return palette[paletteIndex!![blockIndex]]
    }

    fun set(blockPosition: Int3, block: Block) {
        val blockIndex = blockPositionToBlockIndex(blockPosition)
        set(blockIndex, block)
    }

    fun set(blockIndex: Int, block: Block) {
        if (paletteIndex == null) {
            singleBlockType = null
            paletteIndex = PackedArray(arraySize, Config.initialBlockPalletePackedArrayElementSizeInBits)
        }

        for (index in palette.indices) {
            if (palette[index] == block) {
                paletteIndex!![blockIndex] = index
                return
            }
        }
        // Block wasn't in palette
        addNewBlock(block)
        paletteIndex!![blockIndex] = palette.size - 1
    }

    fun addNewBlock(block: Block) {
        palette.add(block)
        if (paletteIndex == null)
            paletteIndex = PackedArray(arraySize, Config.initialBlockPalletePackedArrayElementSizeInBits)
        if (2.pow(paletteIndex!!.bitsPerElement) < palette.size) {
            val newPaletteIndex = PackedArray(arraySize, paletteIndex!!.bitsPerElement + 1)
            for (index in paletteIndex!!.indices) {
                newPaletteIndex[index] = paletteIndex!![index]
            }
            paletteIndex = newPaletteIndex
        }
    }

    fun all(block: Block): Boolean {
        if (singleBlockType == block)
            return true

        val index = palette.indexOf(block)
        require(index != -1) { "Yeah it broke" }
        return paletteIndex!!.all(index)
    }

    fun contains(block: Block): Boolean {
        return block in palette
    }

    private fun blockPositionToBlockIndex(blockPosition: Int3): Int {
        return (blockPosition.x *  Config.chunkSize *  Config.chunkSize) + (blockPosition.y *  Config.chunkSize) + blockPosition.z
    }

    private fun blockIndexToBlockPos(blockIndex: Int): Int3 {
        return Int3(blockIndex / (Config.chunkSize * Config.chunkSize), (blockIndex / Config.chunkSize) % Config.chunkSize, blockIndex % Config.chunkSize)
    }

    fun updateSingleBlockType() {
        val firstBlock = get(0)
        if (all(firstBlock)) {
            singleBlockType = firstBlock
            paletteIndex = null
        } else {
            singleBlockType = null
            if (paletteIndex == null) {
                paletteIndex = PackedArray(arraySize, Config.initialBlockPalletePackedArrayElementSizeInBits)
            }
        }
    }
}
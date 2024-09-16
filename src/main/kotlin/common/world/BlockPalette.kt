package common.world

import common.Config
import common.block.blocks.Block
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
    var singleBlockType: Block? = Block.air

    val blocks: List<Block>
        get() = (0..<arraySize).map { get(it) }
    val blocksWithPositions: List<Pair<Block, Int3>>
        get() = blocks.zip(Chunk.blockPositions)

    fun get(blockPosition: Int3): Block {
        val blockIndex = Chunk.blockPositionToBlockIndex(blockPosition)
        return get(blockIndex)
    }

    fun get(blockIndex: Int): Block {
        if (singleBlockType != null)
            return singleBlockType!!
        return palette[paletteIndex!![blockIndex]]
    }

    fun set(blockPosition: Int3, block: Block) {
        val blockIndex = Chunk.blockPositionToBlockIndex(blockPosition)
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
            println("Block palette size increased from ${paletteIndex!!.bitsPerElement} to ${paletteIndex!!.bitsPerElement + 1}")
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
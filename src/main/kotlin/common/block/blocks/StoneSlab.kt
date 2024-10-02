package common.block.blocks

import common.block.models.SlabModel

class StoneSlab : Block() {
    override val id: UByte = 3u
    override val models = listOf(SlabModel(3))

    override fun copy(): StoneSlab {
        return StoneSlab()
    }

    override fun equals(other: Any?): Boolean {
        return other is StoneSlab
    }
}
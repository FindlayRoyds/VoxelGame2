package common.block.blocks

import common.block.Block

class Stone : Block() {
    override val id: UByte get() = 2u

    override fun copy(): Block {
        return Stone()
    }

    override fun _equals(other: Any?): Boolean {
        return other is Stone
    }
}
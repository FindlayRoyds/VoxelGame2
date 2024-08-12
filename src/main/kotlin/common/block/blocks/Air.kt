package common.block.blocks

import common.block.Block

class Air: Block() {
    override val id: UByte get() = 0u

    override fun copy(): Block {
        return Air()
    }

    override fun _equals(other: Any?): Boolean {
        return other is Air
    }
}
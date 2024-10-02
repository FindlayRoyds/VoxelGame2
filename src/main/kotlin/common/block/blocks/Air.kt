package common.block.blocks

import common.block.models.EmptyModel

class Air: Block() {
    override val id: UByte = 0u
    override val models = listOf(EmptyModel())

    override val isSolid = false

    override fun copy(): Air {
        return Air()
    }

    override fun equals(other: Any?): Boolean {
        return other is Air
    }
}
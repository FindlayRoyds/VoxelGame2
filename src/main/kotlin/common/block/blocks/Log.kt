package common.block.blocks

import common.block.models.BlockModel

class Log : Block() {
    override val id: UByte = 5u
    override val models = listOf(BlockModel(5, 2, 5))

    override fun copy(): Log {
        return Log()
    }

    override fun equals(other: Any?): Boolean {
        return other is Log
    }
}
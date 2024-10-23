package common.block.blocks

import common.block.models.BlockModel

class Log : Block() {
    override val id: UByte = 5u
    override val models = listOf(BlockModel("spruce-log-top", "oak-side-shaded-warmer", "spruce-log-top"))

    override fun copy(): Log {
        return Log()
    }

    override fun equals(other: Any?): Boolean {
        return other is Log
    }
}
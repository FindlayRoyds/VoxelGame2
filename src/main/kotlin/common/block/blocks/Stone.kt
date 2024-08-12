package common.block.blocks

import common.GameEngineProvider
import common.block.Block
import common.math.Int3

class Stone : Block() {
    override val id: UByte get() = 2u

    val flowDirections = listOf<Int3>(Int3(1, -1, 0), Int3(0, -1, -1), Int3(0, -1, 1), Int3(-1, -1, 0))

    override fun update(worldPosition: Int3) {
        val chunkManager = GameEngineProvider.getGameEngine().world.chunkManager
        if (chunkManager.getBlock(worldPosition - Int3(0, 1, 0)) is Air) {
            chunkManager.setBlock(worldPosition, Air())
            chunkManager.setBlock(worldPosition - Int3(0, 1, 0), Stone())
            return
        }
        val shuffledFlowDirections = flowDirections.shuffled()
        for (flowDirection in shuffledFlowDirections) {
            if (chunkManager.getBlock(worldPosition + flowDirection) is Air) {
                chunkManager.setBlock(worldPosition, Air())
                chunkManager.setBlock(worldPosition + flowDirection, Stone())
                return
            }
        }
    }

    override fun copy(): Block {
        return Stone()
    }

    override fun _equals(other: Any?): Boolean {
        return other is Stone
    }
}
package common.block.blocks

import common.GameEngineProvider
import common.block.models.VerticalPlaneCrossModel
import common.math.Int3

class Grass : Block() {
    override val id: UByte = 2u
    override val models = listOf(VerticalPlaneCrossModel(6))

    override val isSolid = false

    override fun update(worldPosition: Int3) {
        val chunkManager = GameEngineProvider.getGameEngine().world.chunkManager
        val blockBelow = chunkManager.getBlock(worldPosition + Int3(0, -1, 0)) ?: return
        if (!blockBelow.isSolid) {
            chunkManager.setBlock(worldPosition, Block.air)
        }
    }

    override fun copy(): Grass {
        return Grass()
    }

    override fun equals(other: Any?): Boolean {
        return other is Grass
    }
}
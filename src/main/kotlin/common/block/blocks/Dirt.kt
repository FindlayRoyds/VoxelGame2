package common.block.blocks

import common.GameEngineProvider
import common.block.models.BlockModel
import common.block.models.Model
import common.math.Int3

class Dirt() : Block() {
    override val id: UByte = 1u
    override val models = listOf(
        BlockModel(1, 0, 4),
        BlockModel(4)
    )

    var isGrassy = false

    constructor(isGrassy: Boolean) : this() {
        this.isGrassy = isGrassy
    }

    override fun update(worldPosition: Int3) {
        if (!isGrassy)
            return
        val chunkManager = GameEngineProvider.getGameEngine().world.chunkManager
        val topBlock = chunkManager.getBlock(worldPosition + Int3.up)
        if (topBlock is Air)
            return
        val newDirt = copy()
        newDirt.isGrassy = false
        chunkManager.setBlock(worldPosition, newDirt)
    }

    override fun getModel(): Model {
        return when (isGrassy) {
            true -> models[0]
            false -> models[1]
        }
    }

    override fun copy(): Dirt {
        val new = Block.dirt
        new.isGrassy = isGrassy
        return new
    }

    override fun equals(other: Any?): Boolean {
        return other is Dirt && isGrassy == (other).isGrassy
    }
}
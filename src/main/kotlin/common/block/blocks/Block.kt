package common.block.blocks

import common.block.models.Model
import common.math.Int3
import java.io.Serializable

abstract class Block: Serializable {
    abstract val id: UByte
    abstract val models: List<Model>

    val name: String
        get() {
            return this::class.simpleName!!
        }

    open fun getModel(): Model {
        return models[0]
    }

    open fun update(worldPosition: Int3) {}

    abstract override fun equals(other: Any?): Boolean
    abstract fun copy(): Block
    override fun hashCode(): Int {
        return id.hashCode()
    }

    companion object {
        val air = Air()
        val dirt = Dirt()
            val dirt_grassy = Dirt(isGrassy = true)
        val stone = Stone()
        val stoneSlab = StoneSlab()

        val blockList = listOf(
            air, dirt, stone, stoneSlab
        )

        val blockNeighbors = arrayListOf(Int3(0, 0, -1), Int3(0, 0, 1), Int3(-1, 0, 0), Int3(1, 0, 0), Int3(0, -1, 0), Int3(0, 1, 0))
    }
}
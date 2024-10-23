package common.block.blocks

import common.block.models.Model
import common.math.Int3
import java.io.Serializable

abstract class Block: Serializable {
    abstract val id: UByte
    abstract val models: List<Model>

    open val isSolid = true

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
        val grass = Grass()
        val log = Log()

        val blockList = listOf(
            air, dirt, stone, stoneSlab, grass, log
        )

        val blockNeighbors = arrayListOf(Int3(0, 0, -1), Int3(0, 0, 1), Int3(-1, 0, 0), Int3(1, 0, 0), Int3(0, -1, 0), Int3(0, 1, 0))
        val blockDiagonalNeighbors = listOf(-1, 0, 1).flatMap { x ->
            listOf(-1, 0, 1).flatMap { y ->
                listOf(-1, 0, 1).map { z ->
                    Int3(x, y, z)
                }
            }
        }.filter { it != Int3(0, 0, 0) }
    }
}
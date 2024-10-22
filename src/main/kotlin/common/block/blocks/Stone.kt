package common.block.blocks

import common.block.models.BlockModel
import common.math.Int3

class Stone : Block() {
    override val id: UByte = 2u
    override val models = listOf(BlockModel(3))

    override fun update(worldPosition: Int3) {
//        val chunkManager = GameEngineProvider.gameEngine.world.chunkManager
//
//        if (chunkManager.getBlock(worldPosition - Int3(0, 1, 0)) is Air) {
//            chunkManager.setBlock(worldPosition, Block.air)
//            chunkManager.setBlock(worldPosition - Int3(0, 1, 0), Block.stone)
//            return
//        }
//
//        if (Random.nextInt(4) != 0) {
//            chunkManager.sendBlockUpdate(worldPosition)
//            return
//        }
//
//        val flowDirections = listOf(Int3(1, -1, 0), Int3(0, -1, -1), Int3(0, -1, 1), Int3(-1, -1, 0))
//        val shuffledFlowDirections = flowDirections.shuffled()
//        for (flowDirection in shuffledFlowDirections) {
//            if (chunkManager.getBlock(worldPosition + flowDirection) is Air) {
//                chunkManager.setBlock(worldPosition, Block.air)
//                chunkManager.setBlock(worldPosition + flowDirection, Block.stone)
//                return
//            }
//        }

//        val world =  GameEngineProvider.gameEngine.world
//        val chunkManager = world.chunkManager
//
//        if (world.tick % 4 != 0) {
//            chunkManager.sendBlockUpdate(worldPosition)
//            return
//        }
//
//        val spreadDirections = listOf(Int3(0, -1, 0), Int3(1, 0, 0), Int3(-1, 0, 0), Int3(0, 0, 1), Int3(0, 0, -1))
//        for (spreadDirection in spreadDirections) {
//            val spreadWorldPosition = worldPosition + spreadDirection
//            if (chunkManager.getBlock(spreadWorldPosition) is Air) {
//                chunkManager.setBlock(spreadWorldPosition, Block.stone)
//            }
//        }
    }

    override fun copy(): Stone {
        return Stone()
    }

    override fun equals(other: Any?): Boolean {
        return other is Stone
    }
}
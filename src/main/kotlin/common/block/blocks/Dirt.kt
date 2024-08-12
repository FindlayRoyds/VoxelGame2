package common.block.blocks

import common.block.Block

class Dirt : Block() {
    override val id: UByte get() = 1u

    var isGrassy = true

//    override fun update(worldPosition: Int3) {
//        val chunkManager = GameEngineProvider.getGameEngine().world.chunkManager
////        println(GameEngineProvider.getGameEngine().isServer())
//        if (chunkManager.getBlock(worldPosition + Int3(0, 1, 0)) !is Air) {
//            chunkManager.setBlock(worldPosition, Stone())
//        }
//    }

    override fun copy(): Block {
        val new = Dirt()
        new.isGrassy = isGrassy
        return new
    }

    override fun _equals(other: Any?): Boolean {
        return other is Dirt && isGrassy == (other).isGrassy
    }
}
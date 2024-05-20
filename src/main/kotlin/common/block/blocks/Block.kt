package common.block.blocks

abstract class Block {
    abstract val id: Int
    abstract val name: String

    fun setBlockData(blockData: Byte) {}
    fun getBlockData(): Byte {return 0}

    companion object {
        private val DIRT_ID = Dirt().id
        private val AIR_ID = Air().id

        fun getBlockById(id: Int): Block {
            return when (id) {
                AIR_ID -> Air()
                DIRT_ID -> Dirt()
                else -> throw IllegalArgumentException("Invalid block ID: $id")
            }
        }
    }
}
package common.block.blocks

abstract class Block {
    abstract val id: Byte
    abstract val name: String

    fun setBlockData(blockData: Char) {}
    fun getBlockData(): Char {return 0.toChar()}

    companion object {
        fun getBlockById(id: Byte): Block {
            return when (id) {
                Air().id -> Air()
                Dirt().id -> Dirt()
                else -> throw IllegalArgumentException("Invalid block ID: $id")
            }
        }
    }
}
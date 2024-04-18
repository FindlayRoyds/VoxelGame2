package common.block

abstract class Block {
    abstract val name: String

    companion object {
        lateinit var name: String private set
    }
}
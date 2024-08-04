package common.block.blocks

import kotlin.random.Random

class Dirt : Block() {
    override val id: Byte
        get() {
            return Random.nextInt().toByte()
        }
    override val name = "Dirt"
}
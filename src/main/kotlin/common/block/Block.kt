package common.block

import common.math.Int3
import java.io.Serializable

abstract class Block: Serializable {
    abstract val id: UByte

    val name: String
        get() {
            return this::class.simpleName!!
        }

    open fun update(worldPosition: Int3) {}

    abstract fun _equals(other: Any?): Boolean
    override fun equals(other: Any?): Boolean {
        return _equals(other)
    }
    abstract fun copy(): Block
    override fun hashCode(): Int {
        return id.hashCode()
    }
}
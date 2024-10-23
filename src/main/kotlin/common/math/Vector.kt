package common.math

import java.io.Serializable

interface Vector<Type: Number> : Serializable {
    val magnitude: Double
    val manhattanMagnitude: Double
    val sign: Vector<Int>
    val abs: Vector<Type>
    val minimumComponent: Vector<Int>
    val displayString: String

    fun ceil(): Vector<Int>
    fun floor(): Vector<Int>
    fun round(): Vector<Int>

    override fun hashCode(): Int
    override fun equals(other: Any?): Boolean
}
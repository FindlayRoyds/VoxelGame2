package common.math

interface Vector<Type: Number> {
    val magnitude: Double
    val sign: Vector<Int>
    val normal: Type

    operator fun plus(other: Vector<Type>): Vector<Type>
    operator fun minus(other: Vector<Type>): Vector<Type>
    operator fun times(other: Vector<Type>): Vector<Type>
    operator fun times(other: Type): Vector<Type>
    operator fun div(other: Vector<Type>): Vector<Type>
    operator fun div(other: Type): Vector<Type>

    operator fun plusAssign(other: Vector<Type>)
    operator fun minusAssign(other: Vector<Type>)
    operator fun timesAssign(other: Vector<Type>)
    operator fun divAssign(other: Vector<Type>)

    override fun hashCode(): Int
    override fun equals(other: Any?): Boolean
    
    fun dot(other: Vector<Type>): Type
}
package common.math

import org.joml.Vector3d
import org.joml.Vector3f
import org.joml.Vector3i
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.math.sqrt

abstract class Vector3<Type: Number>(open val x: Type, open val y: Type, open val z: Type) : Vector<Type> {
    override val magnitude: Double
        get() {
            return sqrt(
                x.toDouble() * x.toDouble()
                    + y.toDouble() * y.toDouble()
                    + z.toDouble() * z.toDouble()
            )
        }
    override val manhattanMagnitude: Double
        get() = x.toDouble() + y.toDouble()
    abstract override val sign: Vector3<Int>
    abstract val xz: Vector2<Type>
    abstract val normal: Vector3<Double>
    abstract override val abs: Vector3<Type>
    abstract override val minimumComponent: Vector3<Int>

    abstract operator fun plus(other: Vector3<Type>): Vector3<Type>
    abstract operator fun minus(other: Vector3<Type>): Vector3<Type>

    abstract operator fun times(other: Int3): Vector3<Type>
    operator fun times(other: Double3): Double3 = Double3(x.toDouble() * other.x, y.toDouble() * other.y, z.toDouble() * other.z)
    abstract operator fun times(other: Int): Vector3<Type>
    operator fun times(other: Double): Double3 = Double3(x.toDouble() * other, y.toDouble() * other, z.toDouble() * other)

    operator fun div(other: Int3): Double3 = Double3(x.toDouble() / other.x, y.toDouble() / other.y, z.toDouble() / other.z)
    operator fun div(other: Double3): Double3 = Double3(x.toDouble() / other.x, y.toDouble() / other.y, z.toDouble() / other.z)
    operator fun div(other: Int): Double3 = Double3(x.toDouble() / other, y.toDouble() / other, z.toDouble() / other)
    operator fun div(other: Double): Double3 = Double3(x.toDouble() / other, y.toDouble() / other, z.toDouble() / other)

    /*
    WARNING rounds towards 0!!
     */
    fun toInt3(): Int3 {
        return Int3(x.toInt(), y.toInt(), z.toInt())
    }
    fun toDouble3(): Double3 {
        return Double3(x.toDouble(), y.toDouble(), z.toDouble())
    }
    fun toVector3d(): Vector3d {
        return Vector3d(x.toDouble(), y.toDouble(), z.toDouble())
    }
    fun toVector3f(): Vector3f {
        return Vector3f(x.toFloat(), y.toFloat(), z.toFloat())
    }
    fun toVector3i(): Vector3i {
        return Vector3i(x.toInt(), y.toInt(), z.toInt())
    }

    override fun ceil(): Int3 {
        return Int3(
            ceil(x.toDouble()).toInt(),
            ceil(y.toDouble()).toInt(),
            ceil(z.toDouble()).toInt()
        )
    }
    override fun floor(): Int3 {
        return Int3(
            floor(x.toDouble()).toInt(),
            floor(y.toDouble()).toInt(),
            floor(z.toDouble()).toInt()
        )
    }
    override fun round(): Int3 {
        return Int3(
            x.toDouble().roundToInt(),
            y.toDouble().roundToInt(),
            z.toDouble().roundToInt()
        )
    }

    abstract override fun hashCode(): Int
    abstract override fun equals(other: Any?): Boolean

    abstract fun dot(other: Vector3<Type>): Type
    abstract operator fun unaryMinus(): Vector3<Type>
}
package common.math

import org.joml.Vector2d
import org.joml.Vector2f
import org.joml.Vector2i
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.math.sqrt

abstract class Vector2<Type: Number>(open val x: Type, open val y: Type) : Vector<Type> {
    override val magnitude: Double
        get() {
            return sqrt(
                x.toDouble() * x.toDouble() + y.toDouble() * y.toDouble()
            )
        }
    abstract override val sign: Vector2<Int>
    abstract val normal: Vector2<Double>
    abstract override val abs: Vector2<Type>
    abstract override val minimumComponent: Vector2<Int>

    // Ideally these would use vector2<type>'s.
    // However JVM type erasure makes this not possible as multiple methods have the same signature.
    abstract operator fun plus(other: Vector2<Type>): Vector2<Type>
    abstract operator fun minus(other: Vector2<Type>): Vector2<Type>

    abstract operator fun times(other: Int2): Vector2<Type>
    abstract operator fun times(other: Double2): Vector2<Double>
    abstract operator fun times(other: Int): Vector2<Type>
    abstract operator fun times(other: Double): Vector<Double>

    abstract operator fun div(other: Int2): Vector2<Double>
    abstract operator fun div(other: Double2): Vector2<Double>
    abstract operator fun div(other: Int): Vector2<Double>
    abstract operator fun div(other: Double): Vector2<Double>

    fun toInt2(): Int2 {
        return Int2(x.toInt(), y.toInt())
    }
    fun toDouble2(): Double2 {
        return Double2(x.toDouble(), y.toDouble())
    }
    fun toVector2d(): Vector2d {
        return Vector2d(x.toDouble(), y.toDouble())
    }
    fun toVector2f(): Vector2f {
        return Vector2f(x.toFloat(), y.toFloat())
    }
    fun toVector2i(): Vector2i {
        return Vector2i(x.toInt(), y.toInt())
    }

    fun toInt3(height: Int): Int3 {
        return Int3(x.toInt(), height, y.toInt())
    }
    fun toDouble3(height: Double): Double3 {
        return Double3(x.toDouble(), height, y.toDouble())
    }

    override fun ceil(): Int2 {
        return Int2(
            ceil(x.toDouble()).toInt(),
            ceil(y.toDouble()).toInt()
        )
    }
    override fun floor(): Int2 {
        return Int2(
            floor(x.toDouble()).toInt(),
            floor(y.toDouble()).toInt()
        )
    }
    override fun round(): Int2 {
        return Int2(
            x.toDouble().roundToInt(),
            y.toDouble().roundToInt()
        )
    }

    abstract override fun hashCode(): Int
    abstract override fun equals(other: Any?): Boolean

    abstract fun dot(other: Vector2<Type>): Type
    abstract operator fun unaryMinus(): Vector2<Type>
}